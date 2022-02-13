package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketSheepMeta extends AbstractPacketAgeableEntityMeta<PacketSheepMeta> {
    private DyeColor woolColor;
    private Boolean sheared;

    public @Nullable Boolean getShearedState() {
        return this.sheared;
    }

    public @NotNull PacketSheepMeta setShearedState(Boolean state) {
        this.sheared = state;
        return this;
    }

    public @NotNull PacketSheepMeta setWoolColor(DyeColor color) {
        this.woolColor = color;
        return this;
    }

    public @Nullable DyeColor getWoolColor() {
        return this.woolColor;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullSheared = this.sheared != null;
        boolean nonNullColor = this.woolColor != null;
        if (nonNullSheared || nonNullColor) {
            data[0].writeByte(17);
            data[0].d(0);
            if (nonNullSheared) data[0].writeByte(this.sheared ? 0x10 : 0x0);
            else data[0].writeByte(this.woolColor.getWoolData());
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketSheepMeta self() {
        return this;
    }
}
