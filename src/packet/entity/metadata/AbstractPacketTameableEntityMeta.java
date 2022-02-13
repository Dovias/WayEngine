package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketTameableEntityMeta<T extends AbstractPacketTameableEntityMeta<T>> extends AbstractPacketMobMeta<T> {
    private Boolean sitting, tamed;

    public @Nullable Boolean getSittingState() {
        return this.sitting;
    }

    public @NotNull T setSittingState(Boolean state) {
        this.sitting = state;
        return this.self();
    }

    public @Nullable Boolean getTamedState() {
        return this.tamed;
    }

    public @NotNull T setTamedState(Boolean state) {
        this.tamed = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullSitting = this.sitting != null;
        boolean nonNullTamed = this.tamed != null;
        if (nonNullSitting || nonNullTamed) {
            byte metaByte = (byte)((nonNullSitting && this.sitting ? 0x1 : 0x0) | (nonNullTamed && this.tamed ? 0x4 : 0x0));
            data[0].writeByte(17);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        return data;
    }
}
