package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import io.netty.buffer.Unpooled;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.PacketPlayerMeta;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.PlayerSkin;
import net.minecraft.network
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.scheduler.CraftScheduler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketPlayerEntity extends AbstractPacketEntity {
    private PacketPlayOutPlayerInfo infoAddPacket;
    private final PacketPlayOutPlayerInfo infoRemovePacket;
    private PacketPlayOutNamedEntitySpawn spawnPacket;

    public PacketPlayerEntity(int entityId, AbstractPacketEntityMeta<?> metadata, Location location) {
        super(entityId, UUID.randomUUID(), EntityType.PLAYER, metadata, location);
        this.compilePlayerMetadata();

        Serializer infoRemoveSerializer = new FriendlyByteBuf(Unpooled.buffer());
        infoRemoveSerializer.a(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e); // Remove player action
        infoRemoveSerializer.d(1); // player data amount.
        infoRemoveSerializer.a(super.getUniqueId()); // NPC UUID
        this.infoRemovePacket = new PacketPlayOutPlayerInfo(infoRemoveSerializer);
    }

    @Override
    public void spawn(Player player) {
        FriendlyByteBuf spawnSerializer = new FriendlyByteBuf(Unpooled.buffer());
        spawnSerializer.d(super.getEntityId());
        spawnSerializer.a(super.getUniqueId());

        // Coords
        spawnSerializer.writeDouble(super.getLocation().getX());
        spawnSerializer.writeDouble(super.getLocation().getY());
        spawnSerializer.writeDouble(super.getLocation().getZ());
        // Rotation in byte format.
        spawnSerializer.writeByte((byte)(super.getLocation().getYaw() * 256.0F / 360.0F));
        spawnSerializer.writeByte((byte)(super.getLocation().getPitch() * 256.0F / 360.0F));

        FriendlyByteBuf rotData = new FriendlyByteBuf(Unpooled.buffer());
        rotData.d(super.getEntityId());
        rotData.writeByte((byte)(super.getLocation().getYaw() * 256.0F / 360.0F));

        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket(this.infoAddPacket);
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(spawnSerializer));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(rotData));

        // VERY SHITTY WAY TO DO THIS. REMOVE ASAP.
        ((CraftScheduler)Bukkit.getScheduler()).scheduleInternalTask(() -> connection.sendPacket(this.infoRemovePacket),  10, "npc-tablist-task");

        super.sendMetadata(player);
    }

    private void compilePlayerMetadata() {

        FriendlyByteBuf infoAddSerializer = new FriendlyByteBuf(Unpooled.buffer());
        infoAddSerializer.a(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a); // Add player action
        infoAddSerializer.d(1); // player data amount.
        infoAddSerializer.a(super.getUniqueId()); // NPC UUID
        // Latency in Milliseconds
        // Gamemode ID
        if (super.getMetadata().hasDisplayName()) {
            String name = this.getMetadata().getDisplayName();
            infoAddSerializer.a(name.length() > 16 ? name.substring(0, 16) : name);
        } else {
            infoAddSerializer.a(String.valueOf(super.getEntityId()));
        }
        // doesnt have custom display name in tablist
        if (super.getMetadata() instanceof PacketPlayerMeta metadata) {
            PlayerSkin skin = metadata.getSkin();
            if (skin != null && skin.getTexture() != null && skin.getSignature() != null) {
                infoAddSerializer.d(1); // Non empty properties array
                infoAddSerializer.a("textures");
                infoAddSerializer.a(skin.getTexture());
                infoAddSerializer.writeByte(1);
                infoAddSerializer.a(skin.getSignature());
            } else {
                infoAddSerializer.d(0);
            }
        } else {
            infoAddSerializer.d(0); // Empty properties array

        }
        infoAddSerializer.writeByte(0); // has custom display name in tablist
        infoAddSerializer.d(0); // Gamemode ID
        infoAddSerializer.d(0); // Latency in Milliseconds
        this.infoAddPacket = new PacketPlayOutPlayerInfo(infoAddSerializer);
    }

    @Override
    public void updateMetadata(Player player) {
        this.compilePlayerMetadata();
        super.updateMetadata(player);
    }

    @Override
    public void sendMetadata(Player player) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket(this.infoRemovePacket);
        connection.sendPacket(this.infoAddPacket);
        connection.sendPacket(this.infoRemovePacket);
        super.sendMetadata(player);
    }
}
