package lt.wayout.minecraft.plugin.wayengine.ui.container;

import lt.wayout.minecraft.plugin.wayengine.ui.UI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface UIContainer<T extends UIContainerView<? extends UIContainer<T>>> extends UI<T> {

    @NotNull
    Collection<? extends UIContainerElement<T>> getElements();

    int getSize();

}
