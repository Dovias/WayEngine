package lt.wayout.minecraft.plugin.wayengine.packet.entity.listener;

import lt.wayout.minecraft.plugin.wayengine.packet.PacketListener;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.HandledPacketEntity;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityHandler;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.event.InteractionAction;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.event.PacketEntityInteractEvent;

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PacketEntityInteractListener extends PacketListener<ClientboundEntityEventPacket> {
   private final PacketEntityHandler handler;

    public PacketEntityInteractListener(PacketEntityHandler handler) {
        super(ClientboundEntityEventPacket.class);
        this.handler = Objects.requireNonNull(handler);

    }

    @Override
    public boolean onPacketReceive(Player player, ClientboundEntityEventPacket packet) {
        CraftServer craftServer = ((CraftServer)Bukkit.getServer());
        craftServer.getHandle().getServer().scheduleOnMain(() -> {
            Entity nmsEntity = packet.getEntity(((CraftPlayer)player).getHandle().getLevel());
            HandledPacketEntity entity = this.handler.getPacketEntity(CraftEntity.getEntity((CraftServer)Bukkit.getServer(), nmsEntity));
            if (entity == null) return;
            Bukkit.getPluginManager().callEvent(new PacketEntityInteractEvent(this.handler, player, entity, InteractionAction.fromNMS(packet.getActionType())));
        });
        return true;
    }

    @NotNull
    public PacketEntityHandler getHandler() {
        return handler;
    }
}
