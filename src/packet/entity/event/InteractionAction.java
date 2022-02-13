package lt.wayout.minecraft.plugin.wayengine.packet.entity.event;

import net.minecraft.network.protocol.game.PacketPlayInUseEntity;

public enum InteractionAction {
    INTERACT,
    ATTACK,
    INTERACT_AT;

    private static final InteractionAction[] values = InteractionAction.values();

    public static InteractionAction fromNMS(PacketPlayInUseEntity.b type) {
        return type.ordinal() < values.length ? values[type.ordinal()] : null;
    }

}
