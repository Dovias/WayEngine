package lt.wayout.minecraft.plugin.wayengine.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface UIView<T extends UI<? extends UIView<T>>> {
    boolean open(@NotNull final Player player);

    boolean close(@NotNull final Player player);

    boolean close();

    @NotNull
    T getUI();

    boolean isViewing(@NotNull final Player player);

    @NotNull
    Collection<Player> getViewers();

    @NotNull
    UUID getUniqueId();
}
