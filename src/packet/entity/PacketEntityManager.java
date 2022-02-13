package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker.PacketTracker;
import lt.wayout.minecraft.plugin.wayengine.util.EventExecutorListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PacketEntityManager {
    private final PacketEntityHandler handler;
    private final List<HandledPacketEntity> persistentEntities = new ArrayList<>();

    public PacketEntityManager(PacketEntityHandler handler) {
        this.handler = handler;
        EventExecutorListener.register(handler.getConnectionReader().getPlugin(), PlayerJoinEvent.class, (PlayerJoinEvent event) -> {
            Player player = event.getPlayer();
            for (HandledPacketEntity entity : this.persistentEntities) {
                for (PacketTracker tracker : entity.getTrackers()) {
                    if (tracker.hasEntity(player)) {
                        entity.setTracker(tracker, player);
                        break;
                    }
                }
                this.handler.addPlayer(player, entity);
            }
        });
    }

    public HandledPacketEntity spawnPacketEntity(EntityType type, AbstractPacketEntityMeta<?> metadata, Location location) {
        return this.spawnPacketEntity(type, metadata, location, Bukkit.getOnlinePlayers());
    }

    public HandledPacketEntity spawnPacketEntity(EntityType entityType, AbstractPacketEntityMeta<?> metadata, Location location, Player player) {
        HandledPacketEntity entity = new HandledPacketEntity(this.handler, this.getPacketEntity(entityType, metadata, location));
        this.handler.addPlayer(player, entity);
        return entity;
    }

    public HandledPacketEntity spawnPacketEntity(EntityType entityType, AbstractPacketEntityMeta<?> metadata, Location location, Collection<? extends Player> players) {
        HandledPacketEntity entity = new HandledPacketEntity(this.handler, this.getPacketEntity(entityType, metadata, location));
        for (Player player : players) {
            this.handler.addPlayer(player, entity);
        }
        return entity;
    }

    public HandledPacketEntity spawnPersistentPacketEntity(EntityType type, AbstractPacketEntityMeta<?> metadata, Location location) {
        return this.spawnPersistentPacketEntity(type, metadata, location, Bukkit.getOnlinePlayers());
    }

    public HandledPacketEntity spawnPersistentPacketEntity(EntityType entityType, AbstractPacketEntityMeta<?> metadata, Location location, Player player) {
        HandledPacketEntity entity = this.spawnPacketEntity(entityType, metadata, location, player);
        this.persistentEntities.add(entity);
        return entity;
    }

    public HandledPacketEntity spawnPersistentPacketEntity(EntityType entityType, AbstractPacketEntityMeta<?> metadata, Location location, Collection<? extends Player> players) {
        HandledPacketEntity entity = this.spawnPacketEntity(entityType, metadata, location, players);
        this.persistentEntities.add(entity);
        return entity;
    }

    @NotNull
    public PacketEntity getPacketEntity(EntityType entityType, AbstractPacketEntityMeta<?> metadata, Location location) {
        return entityType == EntityType.PLAYER ? new PacketPlayerEntity(-ThreadLocalRandom.current().nextInt(), metadata, location) : new PacketLivingEntity(-ThreadLocalRandom.current().nextInt(), entityType, metadata, location);
    }

    @NotNull
    public PacketEntityHandler getHandler() {
        return this.handler;
    }
}
