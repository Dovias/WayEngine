package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.entity.Parrot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketParrotMeta extends AbstractPacketTameableEntityMeta<PacketParrotMeta> {
    private Parrot.Variant variant;

    public @Nullable Parrot.Variant getVariant() {
        return this.variant;
    }

    public @NotNull PacketParrotMeta setVariant(Parrot.Variant variant) {
        this.variant = variant;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.variant != null) {
            data[0].writeByte(19);
            data[0].d(1);
            data[0].d(this.variant.ordinal());
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketParrotMeta self() {
        return this;
    }
}
