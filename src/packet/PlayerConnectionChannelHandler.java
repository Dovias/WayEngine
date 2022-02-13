package lt.wayout.minecraft.plugin.wayengine.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class PlayerConnectionChannelHandler extends ChannelDuplexHandler {
    private final Player player;
    private final Collection<PacketListener<? super Packet<?>>> handlers;

    public PlayerConnectionChannelHandler(Player player, Collection<PacketListener<? super Packet<?>>> handlers){
        if (player == null || !player.isOnline()) {
            // Laikinai nieko nes tingiu.
            throw new IllegalArgumentException();
        }
        this.player = player;
        this.handlers = Objects.requireNonNull(handlers);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet<?>) {
            for (PacketListener<? super Packet<?>> handler : this.handlers) {
                if (!handler.getClazz().isInstance(msg)) continue;
                if (!handler.onPacketSend(player, handler.getClazz().cast(msg))) return;
            }
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet<?>) {
            for (PacketListener<? super Packet<?>> handler : this.handlers) {
                if (!handler.getClazz().isInstance(msg)) continue;
                if (!handler.onPacketReceive(player, handler.getClazz().cast(msg))) return;
            }
        }
        super.channelRead(ctx, msg);
    }
}
