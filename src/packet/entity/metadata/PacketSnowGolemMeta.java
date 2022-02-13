package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketSnowGolemMeta extends AbstractPacketMobMeta<PacketSnowGolemMeta> {
    private Boolean hat;

    public @Nullable Boolean getPumpkinHatState() {
        return this.hat;
    }

    public @NotNull PacketSnowGolemMeta setPumpkinHatState(Boolean state) {
        this.hat = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.hat != null) {
            data[0].writeByte(16);
            data[0].d(0);
            data[0].writeByte(this.hat ? 0x10 : 0x1);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketSnowGolemMeta self() {
        return this;
    }
}
