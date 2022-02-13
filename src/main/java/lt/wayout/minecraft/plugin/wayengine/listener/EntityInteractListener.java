package lt.wayout.minecraft.plugin.wayengine.listener;

import lt.wayout.minecraft.plugin.wayengine.WayEngine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EntityInteractListener implements Listener {
    private final WayEngine plugin;

    public EntityInteractListener(WayEngine plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.getPlayer().getInventory().getItemInMainHand().getType() != Material.STICK /*|| !(event.getRightClicked() instanceof Player)*/) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        Player clickedPlayer = (Player) event.getRightClicked();

        /*PacketLivingEntityMetadata metadata = new PacketBatMetadata();
        metadata.setCollisionState(false);
        metadata.setVisibilityState(false);
        metadata.setGlowingState(true, ChatColor.RED);

        PacketEntityFactory factory = this.plugin.getPacketEntityFactory();
        Set<PacketEntityTracker> trackers = factory.getHandler().getTrackers(clickedPlayer);
        if (trackers == null) {
            clickedPlayer.sendMessage("Spawning tracker.");
            factory.spawnLeashTracker(clickedPlayer, player);
            return;
        }

        for (PacketEntityTracker tracker : trackers) {
            //if (!(tracker instanceof PacketLeashTracker) || ((PacketLeashTracker)tracker).getHolder() != player) continue;
            tracker.stop();
            break;
        }*/


        //EntityInteractListener.this.leash.leashPlayer((Player)event.getRightClicked(), event.getPlayer());
    }

    public WayEngine getPlugin() {
        return this.plugin;
    }
}
