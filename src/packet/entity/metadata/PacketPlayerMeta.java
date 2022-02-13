package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketPlayerMeta extends AbstractPacketLivingEntityMeta<PacketPlayerMeta> {
    private PlayerSkin skin;

    @NotNull
    public PacketPlayerMeta setSkin(PlayerSkin skin) {
        this.skin = skin;
        return this;
    }

    @Nullable
    public PlayerSkin getSkin() {
        return this.skin;
    }

    @Override
    public FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = super.build(entity);
        if (this.skin != null) {
            int metaByte = 0x0;
            if (this.skin.getCapeState() != null) {
                metaByte |= this.skin.getCapeState() ? 0x1 : 0x0;
            }
            if (this.skin.getJacketState() != null) {
                metaByte |= this.skin.getJacketState() ? 0x2 : 0x0;
            }
            if (this.skin.getLeftSleeveState() != null) {
                metaByte |= this.skin.getLeftSleeveState() ? 0x4 : 0x0;
            }
            if (this.skin.getRightSleeveState() != null) {
                metaByte |= this.skin.getRightSleeveState() ? 0x8 : 0x0;
            }
            if (this.skin.getLeftPantsState() != null) {
                metaByte |= this.skin.getRightSleeveState() ? 0x10 : 0x0;
            }
            if (this.skin.getHatState() != null) {
                metaByte |= this.skin.getHatState() ? 0x40 : 0x0;
            }
            if (metaByte != 0x0) {
                data[0].writeByte(17);
                data[0].d(0);
                data[0].writeByte(metaByte);
            }
        }
        if (data[0].writerIndex() == 5) data[0] = null;
        else data[0].writeByte(255);
        return data;
    }

    @Override
    protected @NotNull PacketPlayerMeta self() {
        return this;
    }


}
