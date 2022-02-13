package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GUIContainerItem extends GUIContainerElement {
    private final ItemStack itemStack;

    public GUIContainerItem(ItemStack itemStack) {
        this.itemStack = Preconditions.checkNotNull(itemStack, "ItemStack object cannot be null!");
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
