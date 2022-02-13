package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketVindicatorMeta extends AbstractPacketMobMeta<PacketVindicatorMeta> {
    private Boolean celebrating;

    public @Nullable Boolean getCelabratingState() {
        return this.celebrating;
    }

    public @NotNull PacketVindicatorMeta setCelebratingState(Boolean state) {
        this.celebrating = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.celebrating != null) {
            data[0].writeByte(16);
            data[0].d(7);
            data[0].writeBoolean(this.celebrating);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketVindicatorMeta self() {
        return this;
    }
}
