package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketBeeMeta extends AbstractPacketAgeableEntityMeta<PacketBeeMeta> {
    private Boolean nectar, angry;


    public @Nullable Boolean getNectarState() {
        return this.nectar;
    }

    public @NotNull PacketBeeMeta setNectarState(Boolean state) {
        this.nectar = state;
        return this;
    }

    public @Nullable Boolean getAngryState() {
        return this.angry;
    }

    public @NotNull PacketBeeMeta setAngryState(Boolean state) {
        this.angry = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        byte metaByte = 0x0;
        if (this.angry != null || this.nectar != null) {
            if (this.angry != null) {
                data[0].writeByte(18);
                data[0].d(1);
                if (this.angry) {
                    metaByte |= 0x2;
                    data[0].d(1);
                } else {
                    data[0].d(0);
                }
            }
            if (this.nectar != null) metaByte |= this.nectar ? 0x8 : 0x0;
            data[0].writeByte(17);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketBeeMeta self() {
        return this;
    }
}
