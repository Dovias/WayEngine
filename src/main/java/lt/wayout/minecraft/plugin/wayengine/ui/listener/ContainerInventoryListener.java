package lt.wayout.minecraft.plugin.wayengine.ui.listener;

import com.google.common.base.Preconditions;

import com.google.common.primitives.Ints;
import lt.wayout.minecraft.plugin.wayengine.ui.*;
import lt.wayout.minecraft.plugin.wayengine.ui.container.*;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionContext;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIContainerAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ContainerInventoryListener implements Listener {
    private final UIRegistry manager;

    public ContainerInventoryListener(UIRegistry manager) {
        this.manager = Preconditions.checkNotNull(manager, "GUIManager object cannot be null!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        UI<?> ui = this.manager.getUI(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        GUIContainerView containerView;
        if (!(ui instanceof GUIContainer guiContainer) || !(event.getWhoClicked() instanceof Player player) || (containerView = guiContainer.getView(player)) == null) return;
        InventoryAction action = event.getAction();
        Collection<Integer> slots = Collections.singleton(event.getRawSlot());
        player.sendMessage(action.name());
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
        UI<?> ui = this.manager.getUI(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        GUIContainerView containerView;
        if (!(ui instanceof GUIContainer guiContainer) || !(event.getWhoClicked() instanceof Player player) || (containerView = guiContainer.getView(player)) == null) return;
        event.setCancelled(!containerView.getUI().getHandler().handleItems(new GUIActionContext(containerView, player, event.getRawSlots(), GUIContainerAction.fromBukkit(event.getType()))));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UI<?> ui = this.manager.getUI(UUID.nameUUIDFromBytes(Ints.toByteArray(event.getInventory().hashCode())));
        GUIContainerView containerView;
        if (!(ui instanceof ServerGUIContainer guiContainer) || !(event.getPlayer() instanceof Player player) || (containerView = guiContainer.getView(player)) == null) return;
        containerView.close(player);
        player.sendMessage("Container: " + containerView.getUI().hasView(player));

    }
}
