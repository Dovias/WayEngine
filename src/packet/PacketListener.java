package lt.wayout.minecraft.plugin.wayengine.packet;

import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PacketListener<T extends Packet<?>> {
    private final Class<T> clazz;

    public PacketListener(Class<T> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public boolean onPacketSend(Player player, T packet) {return true;};
    public boolean onPacketReceive(Player player, T packet) {return true;};

}
