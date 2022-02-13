package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.event.entity.EntityMoveEvent;

import lt.wayout.minecraft.plugin.wayengine.packet.ConnectionPipelineReader;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.listener.PacketEntityInteractListener;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker.PacketTracker;
import lt.wayout.minecraft.plugin.wayengine.util.EventExecutorListener;
import lt.wayout.minecraft.plugin.wayengine.util.LocationUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class PacketEntityHandler {
    private final ConnectionPipelineReader reader;

    private final Map<Integer, Map<Integer, Player>> players;
    private final Map<Integer, Map<Integer, HandledPacketEntity>> entities;

    private final Map<Integer, Map<Integer, Player>> stalePlayers;
    private final Map<Integer, Map<Integer, HandledPacketEntity>> staleEntities;


    private final int handlingDistance;

    private <A, B, C, D> boolean addDoubleSyncedMapEntry(Map<A, Map<B, C>> map1, Map<B, Map<A, D>> map2, A key1, B key2, C value1, D value2) {
        return map1.computeIfAbsent(key1, k -> new HashMap<>()).put(key2, value1) == null &&
               map2.computeIfAbsent(key2, k -> new HashMap<>()).put(key1, value2) == null;
    }

    private <A, B, C, D> boolean removeDoubleSyncedMapEntry(Map<A, Map<B, C>> map1, Map<B, Map<A, D>> map2, A key1, B key2) {
        Map<B, C> map1Value = map1.get(key1);
        if (map1Value == null) return false;
        if (map1Value.size() == 1) {
            map1.remove(key1);
        } else {
            map1Value.remove(key2);
        }
        Map<A, D> map2Value = map2.get(key2);
        if (map2Value.size() == 1) {
            map2.remove(key2);
        } else {
            map2Value.remove(key1);
        }
        return true;
    }

    private <A, B, C, D> boolean removeDoubleSyncedMapEntry(Iterator<C> iterator, Map<A, Map<B, C>> map1, Map<B, Map<A, D>> map2,  A key1, B key2) {
        Map<B, C> map1Value = map1.get(key1);
        if (map1Value == null) return false;
        if (map1Value.size() == 1) {
            map1.remove(key1);
        } else {
            iterator.remove();
        }
        Map<A, D> map2Value = map2.get(key2);
        if (map2Value.size() == 1) {
            map2.remove(key2);
        } else {
            map2Value.remove(key1);
        }
        return true;
    }

    private <A, B, C, D> boolean removeAllDoubleSyncedMapEntries(Map<A, Map<B, C>> map1, Map<B, Map<A, D>> map2, A key, Consumer<C> consumer) {
        Map<B, C> map1Value = map1.remove(key);
        if (map1Value == null) return false;
        for (Map.Entry<B, C> entry : map1Value.entrySet()) {
            Map<A, D> map2Value = map2.get(entry.getKey());
            if (map2Value.size() == 1) {
                map2.remove(entry.getKey());
            } else {
                map2Value.remove(key);
            }
            consumer.accept(entry.getValue());
        }
        return true;
    }

    public PacketEntityHandler(ConnectionPipelineReader reader, int handlingDistance) {
        Preconditions.checkArgument(handlingDistance > 0, "Tracking distance cannot be smalller than or equal to 0");
        this.handlingDistance = handlingDistance*handlingDistance;
        this.reader = reader;

        this.players = new HashMap<>();
        this.entities = new HashMap<>();

        this.stalePlayers = new HashMap<>();
        this.staleEntities = new HashMap<>();

        reader.addPacketHandler(new PacketEntityInteractListener(this));
        EventExecutorListener.register(reader.getPlugin(), EntityMoveEvent.class, (event) -> this.tickEntity(event.getEntity()));
        EventExecutorListener.register(reader.getPlugin(), PlayerMoveEvent.class, (event) -> this.tickPlayer(event.getPlayer()));
        EventExecutorListener.register(reader.getPlugin(), PlayerTeleportEvent.class, (event) -> new BukkitRunnable() {
            @Override
            public void run() {
                PacketEntityHandler.this.tickPlayer(event.getPlayer());
            }
        }.runTaskLater(reader.getPlugin(), 1));
        EventExecutorListener.register(reader.getPlugin(), EntityTeleportEvent.class, (event) -> new BukkitRunnable() {
            @Override
            public void run() {
                PacketEntityHandler.this.tickEntity(event.getEntity());
            }
        }.runTaskLater(reader.getPlugin(), 1));
        // FIX THIS!

        EventExecutorListener.register(reader.getPlugin(), PlayerQuitEvent.class, (event) -> this.removePlayer(event.getPlayer()));

        EventExecutorListener.register(reader.getPlugin(), EntityDeathEvent.class, (event) -> {
            Entity entity = event.getEntity();
            Map<Integer, HandledPacketEntity> entities = this.entities.get(entity.getEntityId());
            if (entities == null) return;
            if (!(entity instanceof Player player)) {
                for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext();) {
                    HandledPacketEntity handledEntity = entityIterator.next();
                    PacketTracker tracker = handledEntity.getTracker(entity);
                    if (tracker == null) continue;
                    handledEntity.removeTracker(tracker, entity);

                    this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, entity.getEntityId(), handledEntity.getEntity().getEntityId());
                    this.removePacketEntity(handledEntity);
                }
                entities = this.staleEntities.get(entity.getEntityId());
                for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext();) {
                    HandledPacketEntity handledEntity = entityIterator.next();
                    PacketTracker tracker = handledEntity.getTracker(entity);
                    if (tracker == null) continue;
                    handledEntity.removeTracker(tracker, entity);
                    this.removeDoubleSyncedMapEntry(entityIterator, this.staleEntities, this.stalePlayers, entity.getEntityId(), handledEntity.getEntity().getEntityId());
                    this.removePacketEntity(handledEntity);
                }
                return;
            }
            for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext();) {
                HandledPacketEntity handledEntity = entityIterator.next();
                PacketTracker tracker = handledEntity.getTracker(player);
                if (tracker == null || tracker.isPassive()) continue;
                handledEntity.removeTracker(tracker, player);

                PacketEntity packetEntity = handledEntity.getEntity();
                packetEntity.despawn(player);
                this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, player.getEntityId(), packetEntity.getEntityId());
                this.removePacketEntity(handledEntity);
            }
            entities = this.staleEntities.get(entity.getEntityId());
            for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext();) {
                HandledPacketEntity handledEntity = entityIterator.next();
                PacketTracker tracker = handledEntity.getTracker(player);
                if (tracker == null || tracker.isPassive()) continue;
                handledEntity.removeTracker(tracker, player);
                PacketEntity packetEntity = handledEntity.getEntity();
                this.removeDoubleSyncedMapEntry(entityIterator, this.staleEntities, this.stalePlayers, player.getEntityId(), packetEntity.getEntityId());
                this.removePacketEntity(handledEntity);
            }
        });

    }

    public boolean addPlayer(@NotNull Player player, @NotNull HandledPacketEntity entity) {
        Preconditions.checkNotNull(player, "Player object cannot be null!");
        Preconditions.checkNotNull(entity, "HandledPacketEntity object cannot be null!");

        if (!player.isOnline() || entity.getHandler() != this) return false;
        PacketEntity packetEntity = entity.getEntity();

        if (!this.isEntityInDistance(player, entity)) {
            Bukkit.broadcastMessage("[DEBUG]: Adding stale player: " + player.getName());
            this.addDoubleSyncedMapEntry(this.staleEntities, this.stalePlayers, player.getEntityId(), packetEntity.getEntityId(), entity, player);
            return false;
        }
        if (this.addDoubleSyncedMapEntry(this.entities, this.players, player.getEntityId(), packetEntity.getEntityId(), entity, player)) {
            Bukkit.broadcastMessage("[DEBUG]: Adding player: " + player.getName());
            packetEntity.spawn(player);
            return true;
        }
        return false;
    }

    public boolean removePlayer(@NotNull Player player, @NotNull HandledPacketEntity entity) {
        Preconditions.checkNotNull(player, "Player object cannot be null!");
        Preconditions.checkNotNull(entity, "HandledPacketEntity object cannot be null!");

        PacketEntity packetEntity = entity.getEntity();
        if (this.removeDoubleSyncedMapEntry(this.staleEntities, this.stalePlayers, player.getEntityId(), packetEntity.getEntityId())){
            Bukkit.broadcastMessage("[DEBUG]: Removing stale player: " + player.getName());
            PacketTracker tracker = entity.getTracker(player);
            if (tracker == null) return true;
            if (tracker.isPassive()) {
                entity.removeTracker(tracker, player);
            } else {
                this.removePacketEntity(entity);
            }
            return true;
        } else if (this.removeDoubleSyncedMapEntry(this.entities, this.players, player.getEntityId(), packetEntity.getEntityId())) {
            Bukkit.broadcastMessage("[DEBUG]: Removing player: " + player.getName());
            packetEntity.despawn(player);
            PacketTracker tracker = entity.getTracker(player);
            if (tracker == null) return true;
            if (tracker.isPassive()) {
                entity.removeTracker(tracker, player);
            } else {
                this.removePacketEntity(entity);
            }
            return true;
        }
        return false;
     }

    public boolean removePlayer(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player object cannot be null!");

        boolean staleRemoval = this.removeAllDoubleSyncedMapEntries(this.staleEntities, this.stalePlayers, player.getEntityId(), entity -> {
            Bukkit.broadcastMessage("[DEBUG]: Removing stale player: " + player.getName());
            PacketTracker tracker = entity.getTracker(player);
            if (tracker == null) return;
            if (tracker.isPassive()) {
                entity.removeTracker(tracker, player);
            } else {
                this.removePacketEntity(entity);
            }
        });
        boolean activeRemoval = this.removeAllDoubleSyncedMapEntries(this.entities, this.players, player.getEntityId(), entity -> {
            Bukkit.broadcastMessage("[DEBUG]: Removing player: " + player.getName());
            entity.getEntity().despawn(player);
            PacketTracker tracker = entity.getTracker(player);
            if (tracker == null) return;
            if (tracker.isPassive()) {
                entity.removeTracker(tracker, player);
            } else {
                this.removePacketEntity(entity);
            }
        });
        return staleRemoval || activeRemoval;
    }

    public boolean removePacketEntity(@NotNull HandledPacketEntity entity) {
        Preconditions.checkNotNull(entity, "HandledPacketEntity object cannot be null!");
        boolean staleRemoval = this.removeAllDoubleSyncedMapEntries(this.players, this.entities, entity.getEntity().getEntityId(), player -> {
            Bukkit.broadcastMessage("[DEBUG]: Removing stale player: " + player.getName());
            entity.getEntity().despawn(player);
            PacketTracker tracker = entity.getTracker(player);
            if (tracker != null) entity.removeTracker(tracker, player);
        });
        boolean activeRemoval = this.removeAllDoubleSyncedMapEntries(this.stalePlayers, this.staleEntities, entity.getEntity().getEntityId(), player -> {
            Bukkit.broadcastMessage("[DEBUG]: Removing player: " + player.getName());
            PacketTracker tracker = entity.getTracker(player);
            if (tracker != null) entity.removeTracker(tracker, player);
        });
        return staleRemoval || activeRemoval;
    }

    private boolean removeEntityTrackers(@NotNull HandledPacketEntity packetEntity, @NotNull Entity entity) {
        PacketTracker tracker = packetEntity.getTracker(entity);
        if (tracker == null) return false;
        if (entity instanceof Player) {
            if (tracker.isPassive()) {
                return packetEntity.removeTracker(tracker, entity);
            }
            return packetEntity.removeTracker(tracker) && this.removePacketEntity(packetEntity);
        }
        packetEntity.removeTrackers();
        return this.removePacketEntity(packetEntity);
    }

    @Nullable
    public HandledPacketEntity getPacketEntity(int entityId) {
        Map<Integer, Player> entities = this.players.get(entityId);
        Iterator<Player> iterator;
        if (entities == null || !(iterator = entities.values().iterator()).hasNext()) return null;
        return this.entities.get(iterator.next().getEntityId()).get(entityId);
    }

    @Nullable
    public HandledPacketEntity getPacketEntity(Entity entity) {
        Map<Integer, Player> entities = this.players.get(entity.getEntityId());
        Iterator<Player> iterator;
        if (entities == null || !(iterator = entities.values().iterator()).hasNext()) return null;
        return this.entities.get(iterator.next().getEntityId()).get(entity.getEntityId());
    }

    @Nullable
    public Collection<? extends Player> getPlayers(HandledPacketEntity entity) {
        Map<Integer, Player> playerMap = this.players.get(entity.getEntity().getEntityId());
        return playerMap == null ? null : Collections.unmodifiableCollection(playerMap.values());
    }

    @Nullable
    public Collection<? extends Player> getStalePlayers(HandledPacketEntity entity) {
        Map<Integer, Player> playerMap = this.stalePlayers.get(entity.getEntity().getEntityId());
        return playerMap == null ? null : Collections.unmodifiableCollection(playerMap.values());
    }

    @Nullable
    public Collection<HandledPacketEntity> getPacketEntities(Entity entity) {
        Map<Integer, HandledPacketEntity> entityMap = this.entities.get(entity.getEntityId());
        return entityMap == null ? null : Collections.unmodifiableCollection(entityMap.values());
    }

    @Nullable
    public Collection<HandledPacketEntity> getStalePacketEntities(Entity entity) {
        Map<Integer, HandledPacketEntity> entityMap = this.staleEntities.get(entity.getEntityId());
        return entityMap == null ? null : Collections.unmodifiableCollection(entityMap.values());
    }

    public void tickEntity(@NotNull Entity entity) {
        Preconditions.checkArgument(entity != null && !(entity instanceof Player), "Entity cannot be null and it cannot be an instance of Player interface!");

        Map<Integer, HandledPacketEntity> entities = this.entities.get(entity.getEntityId());
        if (entities == null) return;
        for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext(); ) {
            HandledPacketEntity handledEntity = entityIterator.next();
            PacketEntity packetEntity = handledEntity.getEntity();

            PacketTracker tracker = handledEntity.getTracker(entity);
            if (tracker == null) continue;
            if (!tracker.tick(entity)) {
                this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, entity.getEntityId(), packetEntity.getEntityId());
                if (tracker.isPassive()) {
                    handledEntity.removeTracker(tracker, entity);
                } else {
                    handledEntity.removeTracker(tracker);
                    this.removePacketEntity(handledEntity);
                }
                continue;
            }
            Map<Integer, Player> entityPlayers = this.players.get(packetEntity.getEntityId());
            for (Iterator<Player> playerIterator = entityPlayers.values().iterator();  playerIterator.hasNext();) {
                Player entityPlayer = playerIterator.next();
                if (this.isEntityInDistance(entityPlayer, handledEntity)) {
                    packetEntity.updateLocation(entityPlayer);
                    continue;
                }
                if (!this.removeDoubleSyncedMapEntry(playerIterator, this.players, this.entities, packetEntity.getEntityId(), entityPlayer.getEntityId())) continue;
                packetEntity.despawn(entityPlayer);
                Bukkit.broadcastMessage("[DEBUG]: Adding stale player: " + entityPlayer.getName());
                this.addDoubleSyncedMapEntry(this.staleEntities, this.stalePlayers, entityPlayer.getEntityId(), packetEntity.getEntityId(), handledEntity, entityPlayer);
            }
            entityPlayers = this.stalePlayers.get(packetEntity.getEntityId());
            if (entityPlayers == null) continue;
            for (Iterator<Player> playerIterator = entityPlayers.values().iterator();  playerIterator.hasNext();) {
                Player entityPlayer = playerIterator.next();
                if (!this.isEntityInDistance(entityPlayer, handledEntity) || !this.removeDoubleSyncedMapEntry(playerIterator, this.stalePlayers, this.staleEntities, packetEntity.getEntityId(), entityPlayer.getEntityId())) continue;
                packetEntity.spawn(entityPlayer);
                Bukkit.broadcastMessage("[DEBUG]: Adding player: " + entityPlayer.getName());
                this.addDoubleSyncedMapEntry(this.entities, this.players, entityPlayer.getEntityId(), packetEntity.getEntityId(), handledEntity, entityPlayer);
            }
        }
    }

    public void tickPlayer(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player object cannot be null!");

        Map<Integer, HandledPacketEntity> entities = this.entities.get(player.getEntityId());
        if (entities != null) {
            for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext(); ) {
                HandledPacketEntity handledEntity = entityIterator.next();
                PacketEntity packetEntity = handledEntity.getEntity();
                if (this.isEntityInDistance(player, handledEntity)) {
                    PacketTracker tracker = handledEntity.getTracker(player);
                    if (tracker == null) continue;
                    if (!tracker.tick(player)) {
                        this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, player.getEntityId(), packetEntity.getEntityId());
                        packetEntity.despawn(player);
                        handledEntity.removeTracker(tracker, player);
                        if (!tracker.isPassive()) {
                            this.removePacketEntity(handledEntity);
                        }
                        continue;
                    }
                    Map<Integer, Player> entityPlayers = this.players.get(packetEntity.getEntityId());
                    for (Iterator<Player> playerIterator = entityPlayers.values().iterator();  playerIterator.hasNext();) {
                        Player entityPlayer = playerIterator.next();
                        if (this.isEntityInDistance(entityPlayer, handledEntity)) {
                            packetEntity.updateLocation(entityPlayer);
                            continue;
                        }
                        if (!this.removeDoubleSyncedMapEntry(playerIterator, this.players, this.entities, packetEntity.getEntityId(), entityPlayer.getEntityId())) continue;
                        packetEntity.despawn(entityPlayer);
                        Bukkit.broadcastMessage("[DEBUG]: Adding stale player: " + entityPlayer.getName());
                        this.addDoubleSyncedMapEntry(this.staleEntities, this.stalePlayers, entityPlayer.getEntityId(), packetEntity.getEntityId(), handledEntity, entityPlayer);
                    }
                    entityPlayers = this.stalePlayers.get(packetEntity.getEntityId());
                    if (entityPlayers == null) continue;
                    for (Iterator<Player> playerIterator = entityPlayers.values().iterator();  playerIterator.hasNext();) {
                        Player entityPlayer = playerIterator.next();
                        if (!this.isEntityInDistance(entityPlayer, handledEntity) || !this.removeDoubleSyncedMapEntry(playerIterator, this.stalePlayers, this.staleEntities, packetEntity.getEntityId(), entityPlayer.getEntityId())) continue;
                        packetEntity.spawn(entityPlayer);
                        Bukkit.broadcastMessage("[DEBUG]: Adding player: " + entityPlayer.getName());
                        this.addDoubleSyncedMapEntry(this.entities, this.players, entityPlayer.getEntityId(), packetEntity.getEntityId(), handledEntity, entityPlayer);
                    }
                    continue;
                }
                Bukkit.broadcastMessage("[DEBUG]: Removing player: " + player.getName());
                this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, player.getEntityId(), packetEntity.getEntityId());
                packetEntity.despawn(player);
                Bukkit.broadcastMessage("[DEBUG]: Adding stale player: " + player.getName());
                this.addDoubleSyncedMapEntry(this.staleEntities, this.stalePlayers, player.getEntityId(), packetEntity.getEntityId(), handledEntity, player);
            }
        }
        entities = this.staleEntities.get(player.getEntityId());
        if (entities == null) return;
        for (Iterator<HandledPacketEntity> entityIterator = entities.values().iterator(); entityIterator.hasNext(); ) {
            HandledPacketEntity handledEntity = entityIterator.next();
            PacketEntity packetEntity = handledEntity.getEntity();

            if (!this.isEntityInDistance(player, handledEntity)) continue;
            Bukkit.broadcastMessage("[DEBUG]: Removing stale player: " + player.getName());
            this.removeDoubleSyncedMapEntry(entityIterator, this.staleEntities, this.stalePlayers, player.getEntityId(), packetEntity.getEntityId());
            packetEntity.spawn(player);
            Bukkit.broadcastMessage("[DEBUG]: Adding player: " + player.getName());
            this.addDoubleSyncedMapEntry(this.entities, this.players, player.getEntityId(), packetEntity.getEntityId(), handledEntity, player);

            PacketTracker tracker = handledEntity.getTracker(player);
            if (tracker == null) continue;
            if (!tracker.tick(player)) {
                this.removeDoubleSyncedMapEntry(entityIterator, this.entities, this.players, player.getEntityId(), packetEntity.getEntityId());
                packetEntity.despawn(player);
                handledEntity.removeTracker(tracker, player);
                if (!tracker.isPassive()) {
                    this.removePacketEntity(handledEntity);
                }
                continue;
            }
            Map<Integer, Player> tickingPlayers = this.players.get(packetEntity.getEntityId());
            if (tickingPlayers == null) continue;
            for (Player tickingPlayer : tickingPlayers.values()) {
                packetEntity.updateLocation(tickingPlayer);
            }
        }
    }

    public void removeAllEntities() {
        for (Iterator<Map.Entry<Integer, Map<Integer, HandledPacketEntity>>> entityIterator = this.entities.entrySet().iterator(); entityIterator.hasNext();) {
            Map.Entry<Integer, Map<Integer, HandledPacketEntity>> entityEntry = entityIterator.next();
            entityIterator.remove();
            for (Map.Entry<Integer, HandledPacketEntity> entitySubEntry : entityEntry.getValue().entrySet()) {
                Map<Integer, Player> playerMap = this.players.get(entitySubEntry.getKey());
                PacketEntity entity = entitySubEntry.getValue().getEntity();
                if (playerMap.size() == 1) {
                    entity.despawn(this.players.remove(entitySubEntry.getKey()).values().iterator().next());
                } else {
                    entity.despawn(playerMap.remove(entityEntry.getKey()));
                }
            }
        }
        this.stalePlayers.clear();
        this.staleEntities.clear();
    }

    public boolean isEntityInDistance(Entity entity, HandledPacketEntity packetEntity) {
        return entity.getLocation().getWorld() == packetEntity.getEntity().getLocation().getWorld() && LocationUtils.distance2DSquared(entity.getLocation(), packetEntity.getEntity().getLocation()) <= this.handlingDistance;
    }

    public int getHandlingDistance() {
        return this.handlingDistance;
    }

    @NotNull
    public ConnectionPipelineReader getConnectionReader() {
        return this.reader;
    }
}