package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketAgeableEntityMeta<T extends AbstractPacketAgeableEntityMeta<T>> extends AbstractPacketMobMeta<T> {
    private Boolean baby;

    public @Nullable Boolean getBabyState() {
        return this.baby;
    }

    public @NotNull T setBabyState(Boolean state) {
        this.baby = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.baby != null) {
            data[0].writeByte(16);
            data[0].d(7);
            data[0].writeBoolean(this.baby);
        }
        return data;
    }
}
