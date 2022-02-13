package lt.wayout.minecraft.plugin.wayengine.packet.entity.event;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.HandledPacketEntity;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEntityInteractEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final PacketEntityHandler handler;
    private final Player player;
    private final HandledPacketEntity entity;
    private final InteractionAction action;

    public PacketEntityInteractEvent(PacketEntityHandler handler, Player player, HandledPacketEntity entity, InteractionAction action) {
        this.handler = handler;
        this.player = player;
        this.entity = entity;
        this.action = action;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandledPacketEntity getClickedEntity() {
        return this.entity;
    }

    public InteractionAction getAction() {
        return this.action;
    }

    public PacketEntityHandler getPacketHandler() {
        return this.handler;
    }
}
