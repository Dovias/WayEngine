package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketGuardianMeta extends AbstractPacketMobMeta<PacketGuardianMeta> {
    private Integer target;
    private Boolean retracting;

    public @NotNull PacketGuardianMeta setRetractingSpikesState(Boolean state) {
        this.retracting = state;
        return this;
    }

    public @Nullable Boolean getRetractingSpikesState() {
        return this.retracting;
    }


    public @NotNull PacketGuardianMeta setTarget(Integer target) {
        this.target = target;
        return this;
    }

    public @NotNull Integer getTarget() {
        return this.target;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.retracting != null) {
            data[0].writeByte(16);
            data[0].d(7);
            data[0].writeBoolean(this.retracting);
        }
        if (this.target != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.target);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketGuardianMeta self() {
        return this;
    }
}
