package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.entity.Panda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketPandaMeta extends AbstractPacketAgeableEntityMeta<PacketPandaMeta> {
    private Panda.Gene variant;
    private Boolean sneezing, rolling, sitting, onBack;

    public @Nullable Panda.Gene getVariant() {
        return this.variant;
    }

    public @NotNull PacketPandaMeta setVariant(Panda.Gene gene) {
        this.variant = gene;
        return this;
    }

    public @Nullable Boolean getSneezingState() {
        return this.sneezing;
    }

    public @NotNull PacketPandaMeta setSneezingState(Boolean state) {
        this.sneezing = state;
        return this;
    }

    public @Nullable Boolean getRollingState() {
        return this.rolling;
    }

    public @NotNull PacketPandaMeta setRollingState(Boolean state) {
        this.rolling = state;
        return this;
    }

    public @Nullable Boolean getSittingState() {
        return this.sitting;
    }

    public @NotNull PacketPandaMeta setSittingState(Boolean state) {
        this.sitting = state;
        return this;
    }

    public @Nullable Boolean getOnBackState() {
        return this.onBack;
    }

    public @NotNull PacketPandaMeta setOnBackState(Boolean state) {
        this.onBack = state;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullSneezing = this.sneezing != null;
        boolean nonNullRolling = this.rolling != null;
        boolean nonNullSitting = this.sitting != null;
        boolean nonNullOnBack = this.onBack != null;
        if (nonNullSneezing || nonNullRolling || nonNullSitting || nonNullOnBack) {
            byte metaByte = (byte)((nonNullSitting && this.sitting ? 0x1 : 0x0) |
                    (nonNullSneezing && this.sneezing ? 0x2 : 0x0) |
                    (nonNullRolling && this.rolling ? 0x4 : 0x0) |
                    (nonNullSitting && this.sitting ? 0x8 : 0x0) |
                    (nonNullOnBack && this.onBack ? 0x10 : 0x0));
            data[0].writeByte(22);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        if (this.variant != null) {
            data[0].writeByte(20);
            data[0].d(0);
            data[0].writeByte(this.variant.ordinal());
            data[0].writeByte(21);
            data[0].d(0);
            data[0].writeByte(this.variant.ordinal());
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketPandaMeta self() {
        return this;
    }
}
