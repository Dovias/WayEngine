package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public interface PacketTracker {

    boolean addEntity(@NotNull Entity entity);

    boolean removeEntity(@NotNull Entity entity);

    boolean hasEntity(@NotNull Entity entity);

    @NotNull
    Collection<? extends Entity> getEntities();

    boolean tick(@NotNull Entity entity);

    boolean isPassive();
}
