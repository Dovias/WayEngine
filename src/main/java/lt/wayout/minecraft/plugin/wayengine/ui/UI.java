package lt.wayout.minecraft.plugin.wayengine.ui;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface UI<T extends UIView<? extends UI<T>>> {
    @Nullable
    T open(@NotNull final Player player);

    boolean open(@NotNull final Player player, T view);

    boolean close(@NotNull final Player player);

    boolean close();

    @Nullable
    T getView(@NotNull final Player player);

    boolean hasView(@NotNull final Player player);

    @NotNull
    Collection<Player> getViewers();

    @NotNull
    Collection<T> getViews();

    @NotNull
    UUID getUniqueId();

    @NotNull
    Plugin getPlugin();
}
