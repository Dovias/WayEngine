package lt.wayout.minecraft.plugin.wayengine.ui.container;

import lt.wayout.minecraft.plugin.wayengine.ui.UIView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface UIContainerView<T extends UIContainer<? extends UIContainerView<T>>> extends UIView<T> {
    @NotNull
    Collection<? extends UIContainerElement<? extends UIContainerView<T>>> getElements();
}
