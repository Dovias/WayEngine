package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketArmorStandMeta extends AbstractPacketLivingEntityMeta<PacketArmorStandMeta> {
    private Boolean small, arms, baseplate, marker;
    private EulerAngle headRot, bodyRot, leftArmRot, rightArmRot, leftLegRot, rightLegRot;

    public @Nullable Boolean getSmallState() {
        return this.small;
    }

    public @NotNull PacketArmorStandMeta setSmallState(boolean state) {
        this.small = state;
        return this;
    }

    public @Nullable Boolean getArmsState() {
        return this.arms;
    }

    public @NotNull PacketArmorStandMeta setArmsState(boolean state) {
        this.arms = state;
        return this;
    }

    public @Nullable Boolean getBaseplateState() {
        return this.baseplate;
    }

    public @NotNull PacketArmorStandMeta setBaseplateState(boolean state) {
        this.baseplate = state;
        return this;
    }

    public @Nullable Boolean getMarkerState() {
        return this.marker;
    }

    public @NotNull PacketArmorStandMeta setMarkerState(boolean state) {
        this.marker = state;
        return this;
    }

    public @Nullable EulerAngle getHeadRotation() {
        return this.headRot;
    }

    public @NotNull PacketArmorStandMeta setHeadRotation(EulerAngle angle) {
        this.headRot = angle;
        return this;
    }

    public @Nullable EulerAngle getBodyRotation() {
        return this.bodyRot;
    }

    public @NotNull PacketArmorStandMeta setBodyRotation(EulerAngle angle) {
        this.bodyRot = angle;
        return this;
    }

    public @Nullable EulerAngle getLeftArmRotation() {
        return this.leftArmRot;
    }

    public @NotNull PacketArmorStandMeta setLeftArmRotation(EulerAngle angle) {
        this.leftArmRot = angle;
        return this;
    }

    public @Nullable EulerAngle getRightArmRotation() {
        return this.rightArmRot;
    }

    public @NotNull PacketArmorStandMeta setRightArmRotation(EulerAngle angle) {
        this.rightArmRot = angle;
        return this;
    }

    public @Nullable EulerAngle getLeftLegRotation() {
        return this.leftLegRot;
    }

    public @NotNull PacketArmorStandMeta setLeftLegRotation(EulerAngle angle) {
        this.leftLegRot = angle;
        return this;
    }

    public @Nullable EulerAngle getRightLegRotation() {
        return this.rightLegRot;
    }

    public @NotNull PacketArmorStandMeta setRightLegRotation(EulerAngle angle) {
        this.rightLegRot = angle;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullSmall = this.small != null;
        boolean nonNullArms = this.arms != null;
        boolean nonNullBaseplate = this.baseplate != null;
        boolean nonNullMarker = this.marker != null;
        if (nonNullSmall || nonNullArms || nonNullBaseplate || nonNullMarker) {
            byte metaByte = (byte)((nonNullSmall && this.small ? 0x1 : 0x0) |
                    (nonNullBaseplate && !this.baseplate ? 0x8 : 0x0) |
                    (nonNullMarker && this.marker ? 0x10 : 0x0));
            if (nonNullArms && this.arms) {
                metaByte |= 0x4;
                if (this.leftArmRot != null) this.writeRotationalData(data[0], this.leftArmRot, 18);
                if (this.rightArmRot != null) this.writeRotationalData(data[0], this.rightArmRot, 19);
            }
            data[0].writeByte(15);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        if (this.headRot != null) this.writeRotationalData(data[0], this.headRot, 16);
        if (this.bodyRot != null) this.writeRotationalData(data[0], this.bodyRot, 17);
        if (this.leftLegRot != null) this.writeRotationalData(data[0], this.leftLegRot, 20);
        if (this.rightLegRot != null) this.writeRotationalData(data[0], this.rightLegRot, 21);
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    private void writeRotationalData(FriendlyByteBuf serializer, EulerAngle angle, int index) {
        serializer.writeByte(index);
        serializer.d(8);
        serializer.writeFloat((float) angle.getX());
        serializer.writeFloat((float) angle.getY());
        serializer.writeFloat((float) angle.getZ());
    }

    @Override
    protected @NotNull PacketArmorStandMeta self() {
        return this;
    }
}
