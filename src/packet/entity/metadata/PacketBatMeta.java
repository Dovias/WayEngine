package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketBatMeta extends AbstractPacketMobMeta<PacketBatMeta> {
    private Boolean hanging;

    public @Nullable Boolean getHangingState() {
        return this.hanging;
    }

    public @NotNull PacketBatMeta setHangingState(Boolean state) {
        this.hanging = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.hanging != null) {
            data[0].writeByte(16);
            data[0].d(0);
            data[0].writeBoolean(this.hanging);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketBatMeta self() {
        return this;
    }
}
