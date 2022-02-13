package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketHorseMeta<T extends AbstractPacketHorseMeta<T>> extends AbstractPacketMobMeta<T> {
    private Boolean saddled, eating, rearing, mouth;

    public @Nullable Boolean getSaddledState() {
        return this.saddled;
    }

    public @NotNull T setSaddledState(Boolean saddled) {
        this.saddled = saddled;
        return this.self();
    }

    public @Nullable Boolean getEatingState() {
        return eating;
    }

    public @NotNull T setEatingState(Boolean eating) {
        this.eating = eating;
        return this.self();
    }

    public @Nullable Boolean getRearingState() {
        return this.rearing;
    }

    public @NotNull T setRearingState(Boolean rearing) {
        this.rearing = rearing;
        return this.self();
    }

    public @Nullable Boolean getMouthState() {
        return this.mouth;
    }

    public @NotNull T setMouthState(Boolean mouth) {
        this.mouth = mouth;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullSaddled = this.saddled != null;
        boolean nonNullEating = this.eating != null;
        boolean nonNullRearing = this.rearing != null;
        boolean nonNullMouth = this.mouth != null;
        if (nonNullSaddled || nonNullEating || nonNullRearing || nonNullMouth) {
            byte metaByte = (byte)(
                    (nonNullSaddled && this.saddled ? 0x4 : 0x0) |
                    (nonNullEating && this.eating ? 0x10 : 0x0) |
                    (nonNullRearing && this.rearing ? 0x20 : 0x0) |
                    (nonNullMouth && this.mouth ? 0x40 : 0x0));
            if (metaByte != 0x0) {
                data[0].writeByte(17);
                data[0].d(0);
                data[0].writeByte(metaByte);
            }
        }
        return data;
    }
}
