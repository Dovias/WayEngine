package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketStriderMeta extends AbstractPacketAgeableEntityMeta<PacketStriderMeta> {
    private Boolean saddle, shaking;

    public @Nullable Boolean getSaddleState() {
        return this.saddle;
    }

    public @NotNull PacketStriderMeta setSaddleState(Boolean state) {
        this.saddle = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.saddle != null) {
            data[0].writeByte(19);
            data[0].d(7);
            data[0].writeBoolean(this.saddle);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketStriderMeta self() {
        return this;
    }
}
