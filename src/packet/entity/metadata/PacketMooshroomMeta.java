package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketMooshroomMeta extends AbstractPacketAgeableEntityMeta<PacketMooshroomMeta> {
    private static final String[] types = {"red", "brown"};

    private Integer variant;

    public @Nullable Integer getVariant() {
        return this.variant;
    }

    public @NotNull PacketMooshroomMeta setVariant(Integer variant) {
        this.variant = Math.min(Math.max(variant, 0), 1);
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.variant != null) {
            data[0].writeByte(17);
            data[0].d(3);
            data[0].a(types[this.variant]);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketMooshroomMeta self() {
        return this;
    }
}
