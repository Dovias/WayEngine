package lt.wayout.minecraft.plugin.wayengine.packet.entity.tracker;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPacketEntityTracker extends AbstractPacketTracker {
    private final PacketEntity tracker;

    public AbstractPacketEntityTracker(@NotNull PacketEntity trackerEntity) {
        this(trackerEntity, true);
    }

    public AbstractPacketEntityTracker(@NotNull PacketEntity trackerEntity, boolean isPassive) {
        super(isPassive);
        this.tracker = Preconditions.checkNotNull(trackerEntity, "PacketEntity object cannot be null!");
    }

    @NotNull
    public PacketEntity getTrackerEntity() {
        return this.tracker;
    }
}
