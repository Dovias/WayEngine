package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

import lt.wayout.minecraft.plugin.wayengine.ui.container.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GUIRecalculatedActionHandler extends AbstractGUIActionHandler {

    @Override
    public boolean handleItems(GUIActionContext context) {
        GUIContainerView view = context.getView();

        Player player = context.getPlayer();
        InventoryView bukkitView = player.getOpenInventory();

        Collection<Integer> rawSlots = context.getRawSlots();

        int uiSize = view.getUI().getSize();
        int uiViewSize = view.getSize();

        switch (context.getAction()) {
        case MOVE_TO_OTHER_INVENTORY -> {
            boolean uncancel = true;
            for (int slot : rawSlots) {
                GUIContainerElement element = view.getElement(slot);
                if (element != null) {
                    this.handleItemAnimation(view, player, slot);
                    uncancel = false;
                    continue;
                }

                ItemStack[] viewStacks = new ItemStack[uiSize];
                int stackAmount = 0;
                int airSlot = -1;


                ItemStack clickedStack = bukkitView.getItem(slot);
                if (clickedStack == null) continue;

                // COULD BE OPTIMIZED BY FLAGGING IF THE ITEM IS FOUND WITHIN CONTAINER,
                // IF NOT LET THE CLIENT DO ITS OWN WORK, BUT BY DOING THIS, I'M BREAKING
                // A FEATURE THAT LETS THE SERVER MOVE ITEMS LIKE THE PLAYER'S CLIENT DOES.
                if (slot < uiSize) {
                    for (int i = uiSize; i < uiViewSize; i++) {
                        if (view.hasElement(i)) continue;
                        ItemStack viewStack = bukkitView.getItem(i);
                        if (viewStack == null) {
                            airSlot = i;
                        } else if (viewStack.isSimilar(clickedStack) && viewStack.getAmount() != viewStack.getMaxStackSize()) {
                            viewStacks[stackAmount++] = viewStack;
                        }
                    }
                } else {
                    for (int i = uiSize; --i > -1; ) {
                        if (view.hasElement(i)) continue;
                        ItemStack viewStack = bukkitView.getItem(i);
                        if (viewStack == null) {
                            airSlot = i;
                        } else if (viewStack.isSimilar(clickedStack) && viewStack.getAmount() != viewStack.getMaxStackSize()) {
                            viewStacks[stackAmount++] = viewStack;
                        }
                    }
                }
                if (stackAmount == 0 && airSlot == -1) {
                    uncancel = false;
                    continue;
                }

                for (int i = stackAmount; clickedStack.getAmount() > 0 && --i > -1; ) {
                    int addAmount = Math.min(viewStacks[i].getMaxStackSize() - viewStacks[i].getAmount(), clickedStack.getAmount());
                    viewStacks[i].add(addAmount);
                    clickedStack.subtract(addAmount);
                }
                if (airSlot != -1) {
                    bukkitView.setItem(airSlot, clickedStack);
                    bukkitView.setItem(slot, null);
                }
            }
            return uncancel;
        } case COLLECT_TO_CURSOR -> {
            // Cursor cannot be null, since the COLLECT_TO_CURSOR action has been called.
            ItemStack cursorStack = bukkitView.getCursor();
            if (cursorStack.getAmount() == cursorStack.getMaxStackSize()) return true;

            for (int i = 0; i < uiViewSize; i++) {
                if (!(view.getElement(i) instanceof GUIContainerItem containerItem) || !containerItem.getItemStack().isSimilar(cursorStack)) continue;

                ItemStack fullStack = null;
                for (int j = 0; j < uiViewSize; j++) {
                    ItemStack viewStack = bukkitView.getItem(j);
                    if (viewStack == null || view.hasElement(j) || !viewStack.isSimilar(cursorStack)) continue;
                    if (viewStack.getAmount() == viewStack.getMaxStackSize()) {
                        if (fullStack == null) fullStack = viewStack;
                        continue;
                    }
                    int neededStackAmount = cursorStack.getMaxStackSize() - cursorStack.getAmount();
                    if (viewStack.getAmount() >= neededStackAmount) {
                        cursorStack.add(neededStackAmount);
                        viewStack.subtract(neededStackAmount);
                        return false;
                    }
                    cursorStack.add(viewStack.getAmount());
                    bukkitView.setItem(j, null);
                }
                if (fullStack != null) {
                    int neededStackAmount = cursorStack.getMaxStackSize() - cursorStack.getAmount();
                    cursorStack.add(neededStackAmount);
                    fullStack.subtract(neededStackAmount);
                }
                return false;
            }
            return true;
        } case DRAG_EVEN, DRAG_SINGLE -> {
            for (int slot : rawSlots) {
                if (!view.hasElement(slot)) continue;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Cursor cannot be null, since the event was cancelled.
                        // old cursorStack was returned to the player's cursor.
                        ItemStack cursorStack = bukkitView.getCursor();

                        int splitAmount = context.getAction() == GUIContainerAction.DRAG_EVEN ? cursorStack.getAmount() / rawSlots.size() : 1;
                        ItemStack splitStack = new ItemStack(cursorStack.getType(), splitAmount);
                        for (int slot : rawSlots) {
                            if (view.hasElement(slot)) continue;

                            ItemStack slotStack = bukkitView.getItem(slot);
                            if (slotStack == null) {
                                bukkitView.setItem(slot, splitStack);
                            } else {
                                int neededStackAmount = slotStack.getMaxStackSize() - slotStack.getAmount();
                                if (neededStackAmount < splitAmount) {
                                    slotStack.add(neededStackAmount);
                                    cursorStack.subtract(neededStackAmount);
                                    continue;
                                }
                                slotStack.add(splitAmount);
                            }
                            cursorStack.subtract(splitAmount);
                        }
                    }
                }.runTask(view.getUI().getPlugin());
                return false;
            }
            return true;
        } case PICKUP_ALL, PICKUP_SOME, PICKUP_HALF, PICKUP_ONE, DROP_ALL_SLOT, DROP_ONE_SLOT, SWAP_WITH_CURSOR, CLONE_STACK, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD -> {
            boolean uncancel = true;
            for (int slot : rawSlots) {
               if (!view.hasElement(slot)) continue;
               super.handleItemAnimation(view, player, slot);
               uncancel = false;
           }
           return uncancel;
        } case PLACE_ALL, PLACE_SOME, PLACE_ONE -> {
            for (int slot : rawSlots) {
                if (view.hasElement(slot)) return false;
            }
            return true;
        } default -> {
            return true;
        }}
    }

}
