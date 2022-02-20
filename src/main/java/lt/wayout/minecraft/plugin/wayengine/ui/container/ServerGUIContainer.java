package lt.wayout.minecraft.plugin.wayengine.ui.container;

import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionHandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerGUIContainer extends GUIContainer {
    private final Map<UUID, GUIContainerView> views;

    public ServerGUIContainer(@NotNull Plugin plugin, @NotNull ContainerType type, @Nullable String name, GUIActionHandler handler, List<? extends GUIContainerElement> elements) {
        super(plugin, type, name, handler, elements);
        this.views = new HashMap<>();
    }

    @Override
    public @Nullable GUIContainerView open(@NotNull final Player player, boolean isTransient) {
        GUIContainerView view = new ServerGUIContainerView(this, isTransient);
        return view.open(player) ? this.views.put(view.getUniqueId(), view) : null;
    }

    @Override
    public @Nullable GUIContainerView open(@NotNull final Player player) {
        return this.open(player, true);
    }

    @Override
    public boolean close(@NotNull final Player player) {
        boolean result = false;
        for (GUIContainerView view : this.views.values()) {
            if (view.close(player)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean close(@NotNull final GUIContainerView view) {
        return view.getUI() == this && this.views.remove(view.getUniqueId(), view) && view.close();
    }

    @Override
    public boolean close() {
        boolean result = true;
        for (Iterator<GUIContainerView> iterator = this.views.values().iterator(); iterator.hasNext();) {
            GUIContainerView view = iterator.next();
            if (!view.close()) {
                result = false;
                continue;
            }
            iterator.remove();
        }
        return result;
    }

    @Override
    public @Nullable GUIContainerView getView(@NotNull UUID uuid) {
        return this.views.get(uuid);
    }

    @Override
    public boolean hasView(@NotNull GUIContainerView view) {
        return this.views.containsKey(view.getUniqueId());
    }

    @Override
    public @NotNull Collection<GUIContainerView> getViews() {
        return Collections.unmodifiableCollection(this.views.values());
    }
}
