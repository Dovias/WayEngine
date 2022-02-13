package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketChestedHorseMeta<T extends AbstractPacketChestedHorseMeta<T>> extends AbstractPacketHorseMeta<T> {
    private Boolean chest;

    public @Nullable Boolean getChestState() {
        return this.chest;
    }

    public @NotNull T setChestState(Boolean state) {
        this.chest = state;
        return this.self();
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.chest != null) {
            data[0].writeByte(19);
            data[0].d(7);
            data[0].writeBoolean(this.chest);
        }
        return data;
    }
}
