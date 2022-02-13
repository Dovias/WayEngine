package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker.PacketTracker;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HandledPacketEntity {
    private final PacketEntityHandler handler;
    private final PacketEntity entity;
    private final Map<Integer, PacketTracker> trackers;

    public HandledPacketEntity(@NotNull PacketEntityHandler handler, @NotNull PacketEntity entity) {
        this.handler = Preconditions.checkNotNull(handler, "PacketEntityHandler object cannot be null!");
        this.entity = Preconditions.checkNotNull(entity, "PacketEntity object cannot be null!");
        this.trackers = new HashMap<>();
    }

    public boolean addTracker(@NotNull PacketTracker tracker) {
        Preconditions.checkNotNull(tracker, "PacketEntityTracker object cannot be null!");
        for (Entity entity : tracker.getEntities()) {
            this.trackers.put(entity.getEntityId(), tracker);
        }
        return true;
    }

    public boolean setTracker(@NotNull PacketTracker tracker, @NotNull Entity entity) {
        Preconditions.checkNotNull(tracker, "PacketEntityTracker object cannot be null!");
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.trackers.put(entity.getEntityId(), tracker) == null;
    }

    public boolean removeTracker(@NotNull PacketTracker tracker, @NotNull Entity entity) {
        Preconditions.checkNotNull(tracker, "PacketEntityTracker object cannot be null!");
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.trackers.remove(entity.getEntityId(), tracker);
    }

    public boolean removeTracker(@NotNull PacketTracker tracker) {
        Preconditions.checkNotNull(tracker, "PacketEntityTracker object cannot be null!");
        for (Entity entity : tracker.getEntities()) {
            this.trackers.remove(entity.getEntityId(), tracker);
        }
        return true;
    }

    public void removeTrackers() {
        this.trackers.clear();
    }

    @Nullable
    public PacketTracker getTracker(@NotNull Entity entity) {
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.trackers.get(entity.getEntityId());
    }

    public boolean hasTracker(@NotNull Entity entity) {
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.trackers.get(entity.getEntityId()) != null;
    }

    public boolean hasTracker(@NotNull PacketTracker tracker, @NotNull Entity entity) {
        Preconditions.checkNotNull(tracker, "PacketEntityTracker object cannot be null!");
        Preconditions.checkNotNull(entity, "Entity object cannot be null!");
        return this.trackers.get(entity.getEntityId()) == tracker;
    }

    @NotNull
    public Collection<? extends PacketTracker> getTrackers() {
        return this.trackers.values();
    }

    @NotNull
    public PacketEntity getEntity() {
        return this.entity;
    }

    @NotNull
    public PacketEntityHandler getHandler() {
        return this.handler;
    }
}
