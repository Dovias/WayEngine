package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import io.netty.buffer.Unpooled;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketLivingEntityMeta<T extends AbstractPacketLivingEntityMeta<T>> extends AbstractPacketEntityMeta<T> {
    private Integer holder;

    public @NotNull T setLeashHolder(Integer holder) {
        this.holder = holder;
        return this.self();
    }

    public @Nullable Integer getLeashHolder() {
        return this.holder;
    }


    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.holder != null) {
            data[3] = new FriendlyByteBuf(Unpooled.buffer());
            data[3].writeInt(entity.getEntityId());
            data[3].writeInt(this.holder);
        }
        return data;
    }
}
