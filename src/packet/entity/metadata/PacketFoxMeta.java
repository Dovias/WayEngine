package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketFoxMeta extends AbstractPacketAgeableEntityMeta<PacketFoxMeta> {
    private Integer variant;
    private Boolean sitting, crouching, pouncing, sleeping, faceplanting, defending;

    public @Nullable Integer getVariant() {
        return this.variant;
    }

    public @NotNull PacketFoxMeta setVariant(Integer variant) {
        this.variant = variant;
        return this;
    }

    public @Nullable Boolean getSittingState() {
        return this.sitting;
    }

    public @NotNull PacketFoxMeta setSittingState(Boolean state) {
        this.sitting = state;
        return this;
    }

    public @Nullable Boolean getCrouchingState() {
        return this.crouching;
    }

    public @NotNull PacketFoxMeta setCrouchingState(Boolean state) {
        this.crouching = state;
        return this;
    }

    public @Nullable Boolean getPouncingState() {
        return this.pouncing;
    }

    public @NotNull PacketFoxMeta setPouncingState(Boolean state) {
        this.pouncing = state;
        return this;
    }

    public @Nullable Boolean getSleepingState() {
        return this.sleeping;
    }

    public @NotNull PacketFoxMeta setSleepingState(Boolean state) {
        this.sleeping = state;
        return this;
    }

    public @Nullable Boolean getFaceplantingState() {
        return this.faceplanting;
    }

    public @NotNull PacketFoxMeta setFaceplantingState(Boolean state) {
        this.faceplanting = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.variant != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.variant);
        }
        boolean nonNullSitting = this.sitting != null;
        boolean nonNullCrouching = this.crouching != null;
        boolean nonNullPouncing = this.pouncing != null;
        boolean nonNullSleeping = this.sleeping != null;
        boolean nonNullFaceplanting = this.faceplanting != null;
        if (nonNullSitting || nonNullCrouching || nonNullPouncing || nonNullSleeping || nonNullFaceplanting) {
            byte metaByte = (byte)((nonNullSitting && this.sitting ? 0x1 : 0x0) |
                    (nonNullCrouching && this.crouching ? 0x4 : 0x0) |
                    (nonNullPouncing && this.pouncing ? 0x10 : 0x0) |
                    (nonNullSleeping && this.sleeping ? 0x20 : 0x0) |
                    (nonNullFaceplanting && this.faceplanting ? 0x40 : 0x0));
            data[0].writeByte(18);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketFoxMeta self() {
        return this;
    }
}
