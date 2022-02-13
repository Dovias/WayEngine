package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public final class PacketWitherMeta extends AbstractPacketMobMeta<PacketWitherMeta> {
    private Boolean invulnerable;
    private Integer leftTarget, rightTarget;

    public @NotNull PacketWitherMeta setLeftTarget(Integer target) {
        this.leftTarget = target;
        return this;
    }

    public @NotNull Integer getLeftTarget() {
        return this.leftTarget;
    }

    public @NotNull PacketWitherMeta setRightTarget(Integer target) {
        this.rightTarget = target;
        return this;
    }

    public @NotNull Integer getRightTarget() {
        return this.rightTarget;
    }

    public @NotNull PacketWitherMeta setBlueState(Boolean state) {
        this.invulnerable = state;
        return this;
    }

    public @NotNull Boolean getBlueState() {
        return this.invulnerable;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.leftTarget != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.leftTarget);
        }
        if (this.rightTarget != null) {
            data[0].writeByte(18);
            data[0].d(1);
            data[0].d(this.rightTarget);
        }
        if (this.invulnerable != null) {
            data[0].writeByte(19);
            data[0].d(1);
            data[0].d(this.invulnerable ? 1 : 0);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketWitherMeta self() {
        return this;
    }
}
