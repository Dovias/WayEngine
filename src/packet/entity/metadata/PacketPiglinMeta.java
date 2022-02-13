package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketPiglinMeta extends AbstractPacketMobMeta<PacketPiglinMeta> {
    private Boolean baby, holding, dancing;

    public @Nullable Boolean getBabyState() {
        return this.baby;
    }

    public @NotNull PacketPiglinMeta setBabyState(Boolean state) {
        this.baby = state;
        return this.self();
    }

    public @Nullable Boolean getHoldingState() {
        return this.holding;
    }

    public @NotNull PacketPiglinMeta setHoldingState(Boolean state) {
        this.holding = state;
        return this.self();
    }

    public @Nullable Boolean getDancingState() {
        return this.dancing;
    }

    public @NotNull PacketPiglinMeta setDancingState(Boolean state) {
        this.dancing = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.baby != null) {
            data[0].writeByte(17);
            data[0].d(7);
            data[0].writeBoolean(this.baby);
        }
        if (this.holding != null) {
            data[0].writeByte(18);
            data[0].d(7);
            data[0].writeBoolean(this.holding);
        }
        if (this.dancing != null) {
            data[0].writeByte(19);
            data[0].d(7);
            data[0].writeBoolean(this.dancing);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketPiglinMeta self() {
        return this;
    }
}
