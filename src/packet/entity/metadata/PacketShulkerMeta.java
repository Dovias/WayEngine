package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import lt.wayout.minecraft.plugin.wayengine.util.EntityDirection;

import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketShulkerMeta extends AbstractPacketMobMeta<PacketShulkerMeta> {
    private EntityDirection direction;
    private Integer offset;
    private DyeColor color;

    public @Nullable EntityDirection getAttachmentDirection() {
        return this.direction;
    }

    public @NotNull PacketShulkerMeta setAttachmentDirection(EntityDirection direction) {
        this.direction = direction;
        return this;
    }

    public @Nullable Integer getAttachmentOffset() {
        return this.offset;
    }

    public @NotNull PacketShulkerMeta setAttachmentOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public @Nullable DyeColor getColor() {
        return this.color;
    }

    public @NotNull PacketShulkerMeta setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.direction != null) {
            data[0].writeByte(16);
            data[0].d(11);
            data[0].d(this.direction.ordinal());
        }
        if (this.offset != null) {
            data[0].writeByte(17);
            data[0].d(0);
            data[0].writeByte(this.offset);
        }
        if (this.color != null) {
            data[0].writeByte(18);
            data[0].d(0);
            data[0].writeByte(this.color.ordinal());
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketShulkerMeta self() {
        return this;
    }
}
