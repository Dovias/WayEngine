package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketCreeperMeta extends AbstractPacketMobMeta<PacketCreeperMeta> {
    private Boolean fused, charged;

    public @NotNull PacketCreeperMeta setFusedState(Boolean state) {
        this.fused = state;
        return this;
    }

    public @Nullable Boolean getFusedState() {
        return this.fused;
    }

    public @NotNull PacketCreeperMeta setChargedState(Boolean state) {
        this.charged = state;
        return this;
    }

    public @Nullable Boolean getChargedState() {
        return this.fused;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.fused != null) {
            data[0].writeByte(16);
            data[0].d(1);
            data[0].d(this.fused ? 1: -1);
        }
        if (this.charged != null) {
            data[0].writeByte(17);
            data[0].d(7);
            data[0].writeBoolean(this.charged);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketCreeperMeta self() {
        return this;
    }
}
