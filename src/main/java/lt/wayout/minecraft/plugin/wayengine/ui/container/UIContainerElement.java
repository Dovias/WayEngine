package lt.wayout.minecraft.plugin.wayengine.ui.container;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public interface UIContainerElement<T extends UIContainerView<? extends UIContainer<T>>> {
    void invoke(final @NotNull T uiView, final @NotNull ClickType type, final @NotNull Player player);
}
