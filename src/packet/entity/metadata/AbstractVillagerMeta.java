package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractVillagerMeta<T extends AbstractVillagerMeta<T>> extends AbstractPacketAgeableEntityMeta<T> {
    private Integer shakingTicks;

    public @Nullable Integer getShakingHeadTicks() {
        return this.shakingTicks;
    }

    public @NotNull T setShakingHeadTicks(Integer ticks) {
        this.shakingTicks = ticks;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.shakingTicks != null) {
            data[0].writeByte(17);
            data[0].d(1);
            data[0].d(this.shakingTicks);
        }
        return data;
    }
}
