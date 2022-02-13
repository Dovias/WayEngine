package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketCatMeta extends AbstractPacketTameableEntityMeta<PacketCatMeta> {
    private Cat.Type variant;
    private Boolean lying;
    private DyeColor collarColor;

    public @Nullable Cat.Type getVariant() {
        return this.variant;
    }

    public @NotNull PacketCatMeta setVariant(Cat.Type variant) {
        this.variant = variant;
        return this;
    }

    public @Nullable Boolean getLyingState() {
        return this.lying;
    }

    public @NotNull PacketCatMeta setLyingState(Boolean state) {
        this.lying = state;
        return this;
    }

    public @Nullable DyeColor getCollarColor() {
        return this.collarColor;
    }

    public @NotNull PacketCatMeta setCollarColor(DyeColor color) {
        this.collarColor = color;
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
        if (this.lying != null) {
            data[0].writeByte(20);
            data[0].d(7);
            data[0].writeBoolean(this.lying);
        }
        Boolean tamed = super.getTamedState();
        if (this.collarColor != null && tamed != null && tamed) {
            data[0].writeByte(22);
            data[0].d(1);
            data[0].d(this.collarColor.ordinal());
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketCatMeta self() {
        return this;
    }
}
