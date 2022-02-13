package lt.wayout.minecraft.plugin.wayengine.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface UIView<T extends UI<? extends UIView<T>>> {
    boolean open(@NotNull final Player player);

    boolean close(@NotNull final Player player);

    boolean close();

    @NotNull
    T getUI();
}
