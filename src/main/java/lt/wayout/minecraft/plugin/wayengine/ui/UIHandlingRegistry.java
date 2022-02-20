package lt.wayout.minecraft.plugin.wayengine.ui;

import lt.wayout.minecraft.plugin.wayengine.WayEngine;
import lt.wayout.minecraft.plugin.wayengine.ui.listener.ContainerViewerListener;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UIHandlingRegistry {
    private final Map<UUID, UI<?>> containers;
    private final Plugin plugin;

    public UIHandlingRegistry(@NotNull final WayEngine plugin) {
        this.plugin = plugin;
        this.containers = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new ContainerViewerListener(this), plugin);
    }

    public boolean register(@NotNull final UI<?> ui) {
        for (UIView<?> view : ui.getViews()) {
            this.containers.putIfAbsent(view.getUniqueId(), ui);
        }
        this.containers.put(UUID.randomUUID(), ui);
        return true;
    }

    public boolean unregister(@NotNull final UI<?> ui) {
        boolean result = true;
        for (UIView<?> view : ui.getViews()) {
            if (!this.containers.remove(view.getUniqueId(), ui)) {
                result = false;
            }
        }
        return result;
    }

    @Nullable
    public UI<?> getUI(@NotNull final UUID uuid) {
        UI<?> container = this.containers.get(uuid);
        if (container != null) {
            return container;
        }
        for (UI<?> container1 : this.containers.values()) {
            UIView<?> view = container1.getView(uuid);
            if (view == null) continue;
            this.containers.put(uuid, container1);
            container = container1;
            break;
        }
        return container;
    }

    @Nullable
    public UIView<?> getUIView(@NotNull final UUID uuid) {
        UI<?> ui = this.getUI(uuid);
        return ui == null ? null : ui.getView(uuid);
    }

    @NotNull
    public Collection<UI<?>> getUIs() {
        return Collections.unmodifiableCollection(this.containers.values());
    }

    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }
}
