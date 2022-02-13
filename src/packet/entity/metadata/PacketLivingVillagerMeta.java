package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketLivingVillagerMeta extends AbstractVillagerMeta<PacketLivingVillagerMeta> {
    private Villager.Type variant;
    private Villager.Profession profession;
    private int level;

    public @Nullable Villager.Type getVariant() {
        return this.variant;
    }

    public @NotNull PacketLivingVillagerMeta setVariant(Villager.Type variant) {
        this.variant = variant;
        return this;
    }

    public @Nullable Villager.Profession getProfession() {
        return this.profession;
    }

    public @NotNull PacketLivingVillagerMeta setProfession(Villager.Profession profession) {
        this.profession = profession;
        return this;
    }

    public int getLevel() {
        return this.level;
    }

    public @NotNull PacketLivingVillagerMeta setLevel(int level) {
        this.level = level;
        return this;
    }

    @Override
    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        boolean nonNullVariant = this.variant != null;
        boolean nonNullProfession = this.profession != null;
        if (nonNullVariant || nonNullProfession) {
            data[0].writeByte(18);
            data[0].d(16);
            data[0].d(nonNullVariant ? this.variant.ordinal() : Villager.Type.PLAINS.ordinal());
            data[0].d(nonNullProfession ? this.profession.ordinal() : Villager.Profession.NONE.ordinal());
            data[0].d(this.level);
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketLivingVillagerMeta self() {
        return this;
    }
}
