package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketAxolotlMeta extends AbstractPacketAgeableEntityMeta<PacketAxolotlMeta> {
    private Integer variant;
    private Boolean fakeDeath;

    public @Nullable Integer getVariant() {
        return this.variant;
    }

    public @NotNull PacketAxolotlMeta setVariant(Integer variant) {
        this.variant = variant;
        return this;
    }

    public @Nullable Boolean getFakeDeathState() {
        return this.fakeDeath;
    }

    public @NotNull PacketAxolotlMeta setFakeDeathState(Boolean state) {
        this.fakeDeath = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.variant != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.variant);
        }
        if (this.fakeDeath != null) {
            data[0].writeByte(18);
            data[0].d(7);
            data[0].writeBoolean(this.fakeDeath);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketAxolotlMeta self() {
        return this;
    }
}
