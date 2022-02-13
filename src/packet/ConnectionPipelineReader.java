package lt.wayout.minecraft.plugin.wayengine.packet;

import io.netty.channel.Channel;
import lt.wayout.minecraft.plugin.wayengine.util.EventExecutorListener;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPipelineReader {
    private final Queue<PacketListener<? super Packet<?>>> handlers = new ConcurrentLinkedQueue<>();
    private final JavaPlugin plugin;

    public ConnectionPipelineReader(JavaPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        EventExecutorListener.register(plugin, PlayerJoinEvent.class, (event) -> this.startReader(event.getPlayer()));
    }

    public boolean startReader(Player player) {
        if (player == null || !player.isOnline()) return false;
        Channel channel = ((CraftPlayer)player).getHandle().networkManager.channel;
        if (channel.pipeline().get("pipelineReader") != null) return false;
        channel.pipeline().addBefore("packet_handler", "pipelineReader", new PlayerConnectionChannelHandler(player, this.handlers));
        return true;
    }

    public boolean stopReader(Player player) {
        if (player == null || !player.isOnline()) return false;
        Channel channel = ((CraftPlayer)player).getHandle().networkManager.channel;
        if (channel.pipeline().get("pipelineReader") == null) return false;
        channel.pipeline().remove("pipelineReader");

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean addPacketHandler(PacketListener<?> handler) {
        return this.handlers.add((PacketListener<? super Packet<?>>) handler);
    }

    @NotNull
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
