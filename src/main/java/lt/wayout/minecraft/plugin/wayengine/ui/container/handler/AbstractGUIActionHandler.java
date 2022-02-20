package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

import lt.wayout.minecraft.plugin.wayengine.ui.container.GUIContainerView;
import lt.wayout.minecraft.plugin.wayengine.ui.container.GUIAnimatedContainerItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractGUIActionHandler implements GUIActionHandler {
    private final Set<GUIAnimatedContainerItem> playing = new HashSet<>();

    @Override
    public boolean handleItemAnimations(GUIActionContext context) {
        for (int slot : context.getRawSlots()) {
            this.handleItemAnimation(context.getView(), context.getPlayer(), slot);
        }
        return true;
    }

    public boolean handleItemAnimation(GUIContainerView view, Player player, int rawSlot) {
        if (!view.getUI().hasView(view)) return false;

        InventoryView bukkitView = player.getOpenInventory();
        if (!(view.getElement(rawSlot) instanceof GUIAnimatedContainerItem animatedItem) || !animatedItem.isPlaying() || !AbstractGUIActionHandler.this.playing.add(animatedItem)) {
            return false;
        }
        new BukkitRunnable() {
            private Iterator<ItemStack> iterator = animatedItem.getKeyframeStacks().iterator();

            @Override
            public void run() {
                if (animatedItem.isPaused()) return;
                if (iterator.hasNext() && animatedItem.isPlaying()) {
                    ItemStack keyframeStack = iterator.next();
                    bukkitView.setItem(rawSlot, keyframeStack);
                    return;
                }
                bukkitView.setItem(rawSlot, animatedItem.getItemStack());
                if (animatedItem.isOnRepeat()) {
                    iterator = animatedItem.getKeyframeStacks().iterator();
                    return;
                }
                animatedItem.stop();
                this.cancel();
                AbstractGUIActionHandler.this.playing.remove(animatedItem);

            }
        }.runTaskTimer(view.getUI().getPlugin(), 0, animatedItem.getSpeed());
        return true;
    }
}
