package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import lt.wayout.minecraft.plugin.wayengine.util.LocationUtils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PacketEntityTargetTracker extends AbstractPacketEntityTracker {
    private Entity targeted;
    private final double targetRange;

    public PacketEntityTargetTracker(@NotNull PacketEntity entity) {
        super(entity);
        this.targetRange = 32.0d;
    }

    public PacketEntityTargetTracker(@NotNull PacketEntity entity, double targetingRange) {
        super(entity);
        this.targetRange = targetingRange*targetingRange;
    }

    @Override
    public boolean tick(@NotNull Entity entity) {
        Location trackerLocation = super.getTrackerEntity().getLocation();
        double distance = trackerLocation.distanceSquared(entity.getLocation());
        if (distance > targetRange) {
            if (this.targeted == entity) this.targeted = null;
            return true;
        }
        if (this.targeted == null || this.targeted.isDead()) {
            this.targeted = entity;
        }
        trackerLocation.setDirection(this.targeted.getLocation().subtract(trackerLocation).toVector());
        return true;
    }

    public double getTargetingRange() {
        return Math.sqrt(this.targetRange);
    }
}
