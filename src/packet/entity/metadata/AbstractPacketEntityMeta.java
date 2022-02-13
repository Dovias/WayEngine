package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import io.netty.buffer.Unpooled;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntity;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketPlayerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.IChatBaseComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftChatMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketEntityMeta<T extends AbstractPacketEntityMeta<T>> {
    private final static IChatBaseComponent EMPTY_COMPONENT = IChatBaseComponent.a("");

    private Boolean onFire, visible, silent, collision, nameVisibility;
    private Integer freezingTicks;
    private String name;
    private ChatColor glow;

    public @NotNull T setFireState(Boolean state) {
        this.onFire = state;

        return this.self();
    }

    public Boolean getFireState() {
        return this.onFire;
    }

    public @NotNull T setGlow(ChatColor color) {
        this.glow = color;
        return this.self();
    }

    public @Nullable ChatColor getGlowColor() {
        return this.glow;
    }

    public Boolean getGlowingState() {
        return this.glow != null;
    }

    public @NotNull T setVisibilityState(Boolean state) {
        this.visible = state;
        return this.self();
    }

    public Boolean getVisibilityState() {
        return this.visible;
    }

    public @NotNull T setSilentnessState(Boolean state) {
        this.silent = state;
        return this.self();
    }

    public Boolean getSilentnessState() {
        return this.silent;
    }

    public @NotNull T setFreezingTicks(Integer ticks) {
        this.freezingTicks = ticks;
        return this.self();
    }

    public @Nullable Integer getFreezingTicks() {
        return this.freezingTicks;
    }

    public @NotNull T setCollisionState(Boolean state) {
        this.collision = state;
        return this.self();
    }

    public Boolean getCollisionState() {
        return this.collision;
    }

    public @NotNull T setDisplayName(String name) {
        this.name = name;
        return this.self();
    }

    public @Nullable String getDisplayName() {
        return this.name;
    }

    public @NotNull T setDisplayNameVisibilityState(Boolean state) {
        this.nameVisibility = state;
        return this.self();
    }

    @Nullable
    public Boolean getDisplayNameVisibilityState() {
        return this.nameVisibility;
    }

    public boolean hasDisplayName() {
        return this.name != null;
    }

    protected abstract @NotNull T self();

    public @NotNull FriendlyByteBuf[] build(PacketEntity entity) {
        FriendlyByteBuf[] data = new FriendlyByteBuf[4];

        data[0] = new FriendlyByteBuf(Unpooled.buffer());
        data[0].d(entity.getEntityId());

        boolean nonNullOnFire = this.onFire != null;
        boolean nonNullVisible = this.visible != null;
        boolean nonNullGlow = this.glow != null;
        boolean nonNullNameVisibility = this.nameVisibility != null;
        if (nonNullOnFire || nonNullVisible || nonNullGlow) {
            byte metaByte = (byte)((nonNullOnFire && this.onFire ? 0x1 : 0x0) | (nonNullVisible && !this.visible ? 0x20 : 0x0) | (nonNullGlow ? 0x40 : 0x0));
            data[0].writeByte(0);
            data[0].d(0);
            data[0].writeByte(metaByte);
        }
        if (this.name != null && !(entity instanceof PacketPlayerEntity)) {
            data[0].writeByte(2);
            data[0].d(5);
            data[0].writeByte(1);
            data[0].a(IChatBaseComponent.a(this.name));
            if (nonNullNameVisibility && this.nameVisibility) {
                data[0].writeByte(3);
                data[0].d(7);
                data[0].writeByte(0x1);
            }
        }

        if (this.silent != null) {
            data[0].writeByte(4);
            data[0].d(7);
            data[0].writeBoolean(this.silent);
        }
        if (this.freezingTicks != null) {
            data[0].writeByte(7);
            data[0].d(1);
            data[0].d(this.freezingTicks);
        }
        boolean nonNullCollision = this.collision != null;
        Bukkit.broadcastMessage(ChatColor.AQUA + this.name);

        Bukkit.broadcastMessage(ChatColor.AQUA + String.valueOf(nonNullNameVisibility));

        Bukkit.broadcastMessage(ChatColor.AQUA + String.valueOf(this.nameVisibility));

        if (nonNullCollision || nonNullGlow ||
                !(entity instanceof PacketPlayerEntity) && this.name != null && nonNullNameVisibility && !this.nameVisibility ||
                entity instanceof PacketPlayerEntity && nonNullNameVisibility && !this.nameVisibility) {
            String rawName = String.valueOf(entity.getEntityId());
            Bukkit.broadcastMessage(ChatColor.AQUA + rawName);


            data[1] = new FriendlyByteBuf(Unpooled.buffer());
            data[1].a(rawName);
            data[1].writeByte(0);

            data[1].a(AbstractPacketEntityMeta.EMPTY_COMPONENT);
            data[1].writeByte(0);
            data[1].a(nonNullNameVisibility && !this.nameVisibility ? "never" : "always");
            data[1].a(nonNullCollision ? (this.collision ? "always" : "never") : "always");
            data[1].a(CraftChatMessage.getColor(nonNullGlow ? this.glow : ChatColor.WHITE));
            data[1].a(AbstractPacketEntityMeta.EMPTY_COMPONENT);
            data[1].a(AbstractPacketEntityMeta.EMPTY_COMPONENT);
            data[1].d(1);
            if (entity instanceof PacketPlayerEntity) {
                data[1].a(this.hasDisplayName() ? this.getDisplayName() : rawName);
            } else {
                data[1].a(entity.getUniqueId().toString());
            }
            data[2] = new FriendlyByteBuf(Unpooled.buffer());
            data[2].a(rawName);
            data[2].writeByte(1);
        }

        return data;
    }

}
