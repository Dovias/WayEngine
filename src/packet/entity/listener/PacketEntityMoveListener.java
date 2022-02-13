package lt.wayout.minecraft.plugin.wayengine.packet.entity.listener;

import lt.wayout.minecraft.plugin.wayengine.packet.PacketListener;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityHandler;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityHuman;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PacketEntityMoveListener extends PacketListener<PacketPlayOutEntity> {
    private final PacketEntityHandler handler;

    public PacketEntityMoveListener(PacketEntityHandler handler) {
        super(PacketPlayOutEntity.class);
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public boolean onPacketSend(Player player, PacketPlayOutEntity packet) {
        CraftServer obcServer = ((CraftServer) Bukkit.getServer());
        obcServer.getHandle().getServer().scheduleOnMain(() -> {
            EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
            Entity nmsEntity = packet.a(nmsPlayer.getWorld());

            if (!(nmsEntity instanceof EntityLiving) || nmsEntity instanceof EntityHuman) return;
            this.handler.tickEntity(nmsEntity.getBukkitEntity());
        });
        return true;
    }

    @NotNull
    public PacketEntityHandler getHandler() {
        return this.handler;
    }
}
