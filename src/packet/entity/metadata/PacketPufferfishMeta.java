package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public final class PacketPufferfishMeta extends AbstractPacketMobMeta<PacketPufferfishMeta> {
    private Integer state;

    public Integer getInflationState() {
        return this.state;
    }

    public @NotNull PacketPufferfishMeta setInflationState(Integer state) {
        this.state = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (state != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.state);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketPufferfishMeta self() {
        return this;
    }
}
