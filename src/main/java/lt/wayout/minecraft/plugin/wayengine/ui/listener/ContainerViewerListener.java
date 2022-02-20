package lt.wayout.minecraft.plugin.wayengine.ui.listener;

import com.google.common.base.Preconditions;

import com.google.common.primitives.Ints;
import lt.wayout.minecraft.plugin.wayengine.ui.*;
import lt.wayout.minecraft.plugin.wayengine.ui.container.*;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionContext;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIContainerAction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ContainerViewerListener implements Listener {
    private final UIHandlingRegistry manager;

    public ContainerViewerListener(UIHandlingRegistry manager) {
        this.manager = Preconditions.checkNotNull(manager, "GUIManager object cannot be null!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        UIView<?> ui = this.manager.getUIView(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        if (!(ui instanceof GUIContainerView containerView) || !(event.getWhoClicked() instanceof Player player)) return;
        InventoryAction action = event.getAction();
        Collection<Integer> slots = Collections.singleton(event.getRawSlot());
        switch (action) {
        case UNKNOWN:
            event.setCancelled(true);
            break;
        case DROP_ALL_CURSOR:
        case DROP_ONE_CURSOR:
            event.setCancelled(!containerView.getUI().getHandler().handleItems(new GUIActionContext(containerView, player, Collections.singleton(event.getRawSlot()), GUIContainerAction.fromBukkit(event.getAction()))));
            break;
        case HOTBAR_SWAP:
        case HOTBAR_MOVE_AND_READD:
            slots = List.of(event.getRawSlot(), containerView.getSize() - 9 + event.getHotbarButton());
        default:
            for (int slot : slots) {
                if (slot < 0) continue;
                GUIContainerElement element = containerView.getElement(slot);
                if (element != null && element.testPlayer(player)) {
                    element.invoke(containerView, event.getClick(), player);
                }
            }
            if (action == InventoryAction.NOTHING) break;
            event.setCancelled(!containerView.getUI().getHandler().handleItems(new GUIActionContext(containerView, player, slots, GUIContainerAction.fromBukkit(event.getAction()))));
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        UIView<?> ui = this.manager.getUIView(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        if (!(ui instanceof GUIContainerView containerView) || !(event.getWhoClicked() instanceof Player player)) return;
        event.setCancelled(!containerView.getUI().getHandler().handleItems(new GUIActionContext(containerView, player, event.getRawSlots(), GUIContainerAction.fromBukkit(event.getType()))));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        UIView<?> ui = this.manager.getUIView(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        if (!(ui instanceof GUIContainerView containerView) || !(event.getPlayer() instanceof Player player)) return;
        player.sendMessage(ChatColor.GREEN + String.valueOf(ui.getUI().getViews()));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UIView<?> ui = this.manager.getUIView(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        if (!(ui instanceof GUIContainerView containerView) || !(event.getPlayer() instanceof Player player)) return;
        containerView.close(player);
        player.sendMessage(ChatColor.RED + String.valueOf(ui.getUI().getViews()));
    }
}
