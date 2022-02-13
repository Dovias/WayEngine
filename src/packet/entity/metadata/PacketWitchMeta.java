package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketWitchMeta extends AbstractPacketMobMeta<PacketWitchMeta> {
    private Boolean nose;

    public @Nullable Boolean getNoseMovingState() {
        return this.nose;
    }

    public @NotNull PacketWitchMeta setNoseMovingState(Boolean state) {
        this.nose = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.nose != null) {
            data[0].writeByte(16);
            data[0].d(7);
            data[0].writeBoolean(this.nose);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketWitchMeta self() {
        return this;
    }
}
