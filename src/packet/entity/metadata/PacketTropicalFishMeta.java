package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketTropicalFishMeta extends AbstractPacketMobMeta<PacketTropicalFishMeta> {
    private Integer variant;

    public @Nullable Integer getVariant() {
        return this.variant;
    }

    public @NotNull PacketTropicalFishMeta setVariant(Integer variant) {
        this.variant = variant;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (variant != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.variant);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketTropicalFishMeta self() {
        return this;
    }
}
