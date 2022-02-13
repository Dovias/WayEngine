package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import com.google.common.base.Preconditions;
import io.netty.buffer.Unpooled;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public abstract class AbstractPacketEntity implements PacketEntity {
    private final int entityId;
    private final UUID entityUUID;
    private final PacketPlayOutEntityDestroy destroyPacket;
    private PacketPlayOutScoreboardTeam destroyTeamPacket;
    private PacketPlayOutScoreboardTeam teamPacket;
    private PacketPlayOutEntityMetadata metaPacket;
    private PacketPlayOutAttachEntity attachPacket;
    private final EntityType type;
    private final AbstractPacketEntityMeta<?> metadata;
    private final Location location;

    public AbstractPacketEntity(int entityId, UUID entityUUID, EntityType type, AbstractPacketEntityMeta<?> metadata, Location location) {
        this.type = Preconditions.checkNotNull(type, "EntityType object cannot be null!");
        this.location = Preconditions.checkNotNull(location, "Location object cannot be null!");
        this.metadata = Preconditions.checkNotNull(metadata, "AbstractPacketEntityMeta extending object cannot be null!");
        this.entityUUID = Preconditions.checkNotNull(entityUUID, "UUID object cannot be null!");

        this.entityId = entityId;
        this.destroyPacket = new PacketPlayOutEntityDestroy(this.entityId);

        this.compileMetadata(this.metadata);
    }

    @Override
    public void updateMetadata(Player player) {
        this.compileMetadata(this.metadata);
        this.sendMetadata(player);
    }

    @Override
    public void sendMetadata(Player player) {
        if (player == null || !player.isOnline()) return;
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.a(this.attachPacket);
        if (this.teamPacket != null) {
            Collection<String> teamNames = this.teamPacket.e();
            teamNames.add(player.getName());
            Bukkit.broadcastMessage(teamNames.toString());
            connection.a(this.teamPacket);
            teamNames.remove(player.getName());
        }
        connection.a(this.metaPacket);
    }


    @Override
    public @NotNull AbstractPacketEntityMeta<?> getMetadata() {
        return this.metadata;
    }

    private void compileMetadata(AbstractPacketEntityMeta<?> metadata) {
        this.metaPacket = null;
        this.teamPacket = null;
        this.destroyTeamPacket = null;
        this.attachPacket = null;

        FriendlyByteBuf[] data = metadata.build(this);
        if (data[0] != null) this.metaPacket = new PacketPlayOutEntityMetadata(data[0]);
        if (data[1] != null) {
            this.teamPacket = new PacketPlayOutScoreboardTeam(data[1]);
            this.destroyTeamPacket = new PacketPlayOutScoreboardTeam(data[2]);
        }
        if (data[3] != null) {
            this.attachPacket = new PacketPlayOutAttachEntity(data[3]);
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("Compile results: ");
        Bukkit.broadcastMessage("Metadata lt.wayout.minecraft.plugin.wayengine.packet: " + this.metaPacket);
        Bukkit.broadcastMessage("Team lt.wayout.minecraft.plugin.wayengine.packet: " + this.teamPacket);
        Bukkit.broadcastMessage("Team Destroy lt.wayout.minecraft.plugin.wayengine.packet: " + this.destroyTeamPacket);
        Bukkit.broadcastMessage("Attach leash lt.wayout.minecraft.plugin.wayengine.packet: " + this.attachPacket);
        Bukkit.broadcastMessage("");

    }

    @Override
    public void despawn(Player player) {
        if (player == null || !player.isOnline()) return;
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.a(this.destroyPacket);
        connection.a(this.destroyTeamPacket);
    }

    /*public void updateLocationRelatively(Collection<? extends Player> players, Location location) {
        if (players == null || location == null) return;
        this.location = location;

        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook posPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                this.entityId,
                (short)((location.getX() * 32 - this.location.getX() * 32) * 128),
                (short)((location.getY() * 32 - this.location.getY() * 32) * 128),
                (short)((location.getZ() * 32 - this.location.getZ() * 32) * 128),
                (byte)((location.getYaw()) * 256.0F / 360.0F),
                (byte)((location.getPitch()) * 256.0F / 360.0F),
                true
        );

        FriendlyByteBuf rotData = new FriendlyByteBuf(Unpooled.buffer());
        rotData.d(this.entityId);
        rotData.writeByte((byte)(location.getYaw() * 256.0F / 360.0F));
        PacketPlayOutEntityHeadRotation rotPacket = new PacketPlayOutEntityHeadRotation(rotData);

        for (Player player : players) {
            if (!player.isOnline()) continue;
            PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
            connection.sendPacket(posPacket);
            connection.sendPacket(rotPacket);
        }
    }

    public void updateLocationRelatively(Player player, Location location) {
        if (player == null || !player.isOnline() || location == null) return;
        this.location = location;

        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook posPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                this.entityId,
                (short)((location.getX() * 32 - this.location.getX() * 32) * 128),
                (short)((location.getY() * 32 - this.location.getY() * 32) * 128),
                (short)((location.getZ() * 32 - this.location.getZ() * 32) * 128),
                (byte)((location.getYaw()) * 256.0F / 360.0F),
                (byte)((location.getPitch()) * 256.0F / 360.0F),
                true
        );

        FriendlyByteBuf rotData = new FriendlyByteBuf(Unpooled.buffer());
        rotData.d(this.entityId);
        rotData.writeByte((byte)(location.getYaw() * 256.0F / 360.0F));
        PacketPlayOutEntityHeadRotation rotPacket = new PacketPlayOutEntityHeadRotation(rotData);

        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket(posPacket);
        connection.sendPacket(rotPacket);
    }*/

    public void updateLocation(Player player) {
        if (player == null || !player.isOnline() || this.location == null || this.location.getWorld() != player.getWorld()) return;
        FriendlyByteBuf posData = new FriendlyByteBuf(Unpooled.buffer());
        posData.d(this.entityId);
        posData.writeDouble(location.getX());
        posData.writeDouble(location.getY());
        posData.writeDouble(location.getZ());
        posData.writeByte((byte)((location.getYaw()) * 256.0F / 360.0F));
        posData.writeByte((byte)((location.getPitch()) * 256.0F / 360.0F));
        posData.writeBoolean(true);

        FriendlyByteBuf rotData = new FriendlyByteBuf(Unpooled.buffer());
        rotData.d(this.entityId);
        rotData.writeByte((byte)(location.getYaw() * 256.0F / 360.0F));

        PacketPlayOutEntityTeleport posPacket = new PacketPlayOutEntityTeleport(posData);
        PacketPlayOutEntityHeadRotation rotPacket = new PacketPlayOutEntityHeadRotation(rotData);

        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.a(posPacket);
        connection.a(rotPacket);
    }


    public @NotNull Location getLocation() {
        return this.location;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.entityUUID;
    }

    @Override
    public @NotNull EntityType getEntityType() {
        return this.type;
    }
}
