package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketBlazeMeta extends AbstractPacketMobMeta<PacketBlazeMeta> {
    private Boolean fire;

    @Override
    public @NotNull PacketBlazeMeta setFireState(Boolean state) {
        this.fire = state;
        return this;
    }

    @Override
    public @Nullable Boolean getFireState() {
        return this.fire;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.fire != null) {
            data[0].writeByte(16);
            data[0].d(0);
            data[0].writeByte(this.fire ? 0x1 : 0x0);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketBlazeMeta self() {
        return this;
    }
}
