package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import com.google.common.base.Preconditions;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketEntityFollowTracker extends PacketEntityMovementTracker {
    private final Entity followedEntity;

    public PacketEntityFollowTracker(@NotNull PacketEntity entity, @NotNull Entity trackedEntity, @NotNull Entity followedEntity) {
        this(entity, trackedEntity, followedEntity, null);
    }
    public PacketEntityFollowTracker(@NotNull PacketEntity entity, @NotNull Entity trackedEntity, @NotNull Entity followedEntity, @Nullable Location offsetLocation) {
        super(entity, trackedEntity, offsetLocation);
        this.followedEntity = Preconditions.checkNotNull(followedEntity, "Entity object cannot be null!");
        super.addEntity(followedEntity);
    }

    @Override
    public boolean tick(@NotNull Entity entity) {
        Location holderLocation = followedEntity.getLocation();
        Location entityLocation = super.getTrackedEntity().getLocation();
        if (entityLocation.getWorld() != holderLocation.getWorld()) return false;
        if (entityLocation.distanceSquared(holderLocation) > 64) {
            Bukkit.broadcastMessage("[DEBUG]: Follow broke up due to high distance!");
            return false;
        } else if (entityLocation.distanceSquared(holderLocation) > 32) {
            super.getTrackedEntity().setVelocity(holderLocation.toVector().subtract(entityLocation.toVector()).multiply(0.05));
        }
        Location trackerLocation = super.getTrackerEntity().getLocation();
        trackerLocation.set(entityLocation.getX() + super.getOffsetLocation().getX(),
                entityLocation.getY() + super.getOffsetLocation().getY(),
                entityLocation.getZ() + super.getOffsetLocation().getZ());
        trackerLocation.setPitch(entityLocation.getPitch() + super.getOffsetLocation().getPitch());
        trackerLocation.setYaw(entityLocation.getYaw() + super.getOffsetLocation().getYaw());
        return true;
    }


    @NotNull
    public Entity getFollowedEntity() {
        return this.followedEntity;
    }
}
