package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@Deprecated
public class PacketServerEntityAdapter extends AbstractPacketEntity {
    private final Entity entity;

    public PacketServerEntityAdapter(@NotNull Entity entity, @NotNull AbstractPacketEntityMeta<?> metadata) {
        super(entity.getEntityId(), entity.getUniqueId(), entity.getType(), metadata, entity.getLocation());
        this.entity = entity;
    }

    @Override
    public void spawn(Player player) {
        super.sendMetadata(player);
    }

    @Override
    public void despawn(Player player) {}


    @Override
    public void updateLocation(Player player) {
        Location adapterLocation = super.getLocation();
        entity.getLocation().set(adapterLocation.getX(), adapterLocation.getY(), adapterLocation.getZ());
        entity.getLocation().setPitch(adapterLocation.getPitch());
        entity.getLocation().setYaw(adapterLocation.getYaw());
    }

    @NotNull
    public Entity getEntity() {
        return this.entity;
    }
}
