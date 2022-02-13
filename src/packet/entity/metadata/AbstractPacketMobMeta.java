package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketMobMeta<T extends AbstractPacketMobMeta<T>> extends AbstractPacketLivingEntityMeta<T> {
    private Boolean leftHanded;


    public @Nullable Boolean getLeftHandState() {
        return this.leftHanded;
    }

    public @NotNull T setLeftHandState(Boolean state) {
        this.leftHanded = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.leftHanded != null) {
            data[0].writeByte(15);
            data[0].d(0);
            data[0].writeByte(this.leftHanded ? 0x2 : 0x0);
        }
        return data;
    }
}
