package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import com.google.common.base.Preconditions;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketEntityMovementTracker extends AbstractPacketEntityTracker {
    private final Location offsetLocation;
    private Entity tracked;

    public PacketEntityMovementTracker(@NotNull PacketEntity entity, @NotNull Entity trackedEntity) {
        this(entity, trackedEntity, null);
    }

    public PacketEntityMovementTracker(@NotNull PacketEntity entity, @NotNull Entity trackedEntity, @Nullable Location offsetLocation) {
        super(entity, false);
        this.tracked = Preconditions.checkNotNull(trackedEntity, "Entity object cannot be null!");
        this.offsetLocation = offsetLocation == null ? new Location(null, 0,0,0, 0.0F, 0.0F) : offsetLocation;

        Location trackedLocation = this.tracked.getLocation();
        this.getTrackedEntity().getLocation().set(trackedLocation.getX() + this.offsetLocation.getX(),
                trackedLocation.getY() + this.offsetLocation.getY(),
                trackedLocation.getZ() + this.offsetLocation.getZ());
        super.addEntity(this.tracked);
        //TODO: KAZKOKIU BUDU ENTITY TRACKERIS NULAUZE ZMOGENUI RANKA. ISANALIZUOT IR ISNAUDOT SPRAGA.
        // ATRODO KAD ISIUNCIANT SPAWN ENTITY LIVING PAKETA SU TUO PACIU ZAIDEJO EID KLIENTA SUFREAKOUT'INA. ir persukus zaidejo yaw i didele reiksme dingsta jo ranka.
    }

    public boolean tick(@NotNull Entity entity) {
        Location entityLocation = this.tracked.getLocation();
        Location location = super.getTrackerEntity().getLocation();
        if (location.getWorld() != entityLocation.getWorld() && this.tracked instanceof Player player) {
            location.setWorld(entityLocation.getWorld());
            this.getTrackerEntity().spawn(player);
        }
        location.set(entityLocation.getX() + this.offsetLocation.getX(), entityLocation.getY() + this.offsetLocation.getY(), entityLocation.getZ() + this.offsetLocation.getZ());
        location.setPitch(entityLocation.getPitch() + this.offsetLocation.getPitch());
        location.setYaw(entityLocation.getYaw() + this.offsetLocation.getYaw());
        return true;
    }

    @NotNull
    public Entity getTrackedEntity() {
        return this.tracked;
    }

    public void setTrackedEntity(Entity entity) {
        this.tracked = entity;
        Location trackedLocation = this.tracked.getLocation();
        this.getTrackedEntity().getLocation().set(trackedLocation.getX() + this.offsetLocation.getX(),
                trackedLocation.getY() + this.offsetLocation.getY(),
                trackedLocation.getZ() + this.offsetLocation.getZ());
    }

    @NotNull
    public Location getOffsetLocation() {
        return this.offsetLocation;
    }
}
