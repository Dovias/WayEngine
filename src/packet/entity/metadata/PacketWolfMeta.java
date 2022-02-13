package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketWolfMeta extends AbstractPacketTameableEntityMeta<PacketWolfMeta> {
    private Boolean angry, begging;
    private DyeColor collarColor;

    public @Nullable Boolean getAngryState() {
        return this.angry;
    }

    public @NotNull PacketWolfMeta setAngryState(Boolean state) {
        this.angry = state;
        return this;
    }

    public @Nullable Boolean getBeggingState() {
        return this.begging;
    }

    public @NotNull PacketWolfMeta setBeggingState(Boolean state) {
        this.begging = state;
        return this;
    }

    public @Nullable DyeColor getCollarColor() {
        return this.collarColor;
    }

    public @NotNull PacketWolfMeta setCollarColor(DyeColor color) {
        this.collarColor = color;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.begging != null) {
            data[0].writeByte(19);
            data[0].d(7);
            data[0].writeBoolean(this.begging);
        }
        Boolean tamed = super.getTamedState();
        if (tamed != null && tamed) {
            if (this.collarColor != null) {
                data[0].writeByte(20);
                data[0].d(1);
                data[0].d(this.collarColor.getWoolData());
            }
        } else if (this.angry != null) {
            Bukkit.broadcastMessage("Angry setting");
            data[0].writeByte(21);
            data[0].d(1);
            data[0].d(this.angry ? 40 : 0);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketWolfMeta self() {
        return this;
    }
}
