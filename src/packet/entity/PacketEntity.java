package lt.wayout.minecraft.plugin.wayengine.packet.entity;


import lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata.AbstractPacketEntityMeta;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface PacketEntity {

    void spawn(Player player);

    void updateMetadata(Player player);

    void sendMetadata(Player player);

    void despawn(Player player);

    void updateLocation(Player player);

    @NotNull
    Location getLocation();

    @NotNull
    AbstractPacketEntityMeta<?> getMetadata();

    int getEntityId();

    @NotNull
    UUID getUniqueId();

    @NotNull
    EntityType getEntityType();

}
