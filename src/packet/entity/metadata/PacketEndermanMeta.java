package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public final class PacketEndermanMeta extends AbstractPacketTameableEntityMeta<PacketEndermanMeta> {
    private Material material;
    private Boolean screaming, staring;

    public @Nullable Material getHoldingMaterial() {
        return this.material;
    }

    public @NotNull PacketEndermanMeta setHoldingMaterial(Material material) {
        this.material = material;
        return this;
    }

    public @Nullable Boolean getScreamingState() {
        return this.screaming;
    }

    public @NotNull PacketEndermanMeta setScreamingState(Boolean state) {
        this.screaming = state;
        return this;
    }

    public @Nullable Boolean getStaringState() {
        return this.staring;
    }

    public @NotNull PacketEndermanMeta setStaringState(Boolean state) {
        this.staring = state;
        return this;
    }


    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.screaming != null) {
            data[0].writeByte(17);
            data[0].d(7);
            data[0].writeBoolean(this.screaming);
        } else if (this.staring != null) {
            data[0].writeByte(18);
            data[0].d(7);
            data[0].writeBoolean(this.staring);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketEndermanMeta self() {
        return this;
    }
}
