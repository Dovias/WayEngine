package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import io.netty.buffer.Unpooled;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;

import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PacketLivingEntity extends AbstractPacketEntity {
    public PacketLivingEntity(int entityId, EntityType type, AbstractPacketEntityMeta<?> metadata, Location location) {
        super(entityId, UUID.randomUUID(), type, metadata, location);
        Validate.notNull(type, "Failed to construct PacketLivingEntity object! PacketLivingEntityType enum object cannot be null!");
    }

    @Override
    public void spawn(Player player) {
        if (player == null || !player.isOnline()) return;
        ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        nmsPlayer.networkManager.send(this.constructSpawnPacket());
        this.sendMetadata(player);
    }

    @NotNull
    private PacketPlayOutSpawnEntityLiving constructSpawnPacket() {
        FriendlyByteBuf entityData = new FriendlyByteBuf(Unpooled.buffer());
        entityData.d(this.getEntityId());
        entityData.a(this.getUniqueId());
        entityData.d(IRegistry.Y.getId(IRegistry.Y.get(CraftNamespacedKey.toMinecraft(this.getEntityType().getKey()))));
        entityData.writeDouble(this.getLocation().getX());
        entityData.writeDouble(this.getLocation().getY());
        entityData.writeDouble(this.getLocation().getZ());
        entityData.writeByte((int)(this.getLocation().getYaw() * 256.0F / 360.0F));
        entityData.writeByte((int)(this.getLocation().getPitch() * 256.0F / 360.0F));
        entityData.writeByte((int)(this.getLocation().getYaw() * 256.0F / 360.0F));
        entityData.writeShort(0);
        entityData.writeShort(0);
        entityData.writeShort(0);
        return new PacketPlayOutSpawnEntityLiving(entityData);
    }
}
