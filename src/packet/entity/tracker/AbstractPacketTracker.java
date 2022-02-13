package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import com.google.common.base.Preconditions;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractPacketTracker implements PacketTracker {
    private final Map<UUID, Entity> tracked;
    private final boolean passive;

    public AbstractPacketTracker() {
        this(true);
    }

    public AbstractPacketTracker(boolean isPassive) {
        this.tracked = new HashMap<>();
        this.passive = isPassive;
    }

    @Override
    public boolean addEntity(@NotNull Entity entity) {
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.tracked.put(entity.getUniqueId(), entity) != entity;
    }

    @Override
    public boolean removeEntity(@NotNull Entity entity) {
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.tracked.remove(entity.getUniqueId()) != null;
    }

    @Override
    public boolean hasEntity(@NotNull Entity entity) {
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.tracked.get(entity.getUniqueId()) != null;
    }

    @Override
    public @NotNull Collection<? extends Entity> getEntities() {
        return Collections.unmodifiableCollection(this.tracked.values());
    }

    @Override
    public boolean isPassive() {
        return this.passive;
    }
}
