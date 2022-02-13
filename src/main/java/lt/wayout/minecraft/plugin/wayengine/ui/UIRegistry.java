package lt.wayout.minecraft.plugin.wayengine.ui;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.WayEngine;
import lt.wayout.minecraft.plugin.wayengine.ui.listener.ContainerInventoryListener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UIRegistry {
    private final Map<UUID, UI<?>> containers;
    private final Plugin plugin;

    public UIRegistry(@NotNull WayEngine plugin) {
        this.plugin = Preconditions.checkNotNull(plugin, "Plugin object cannot be null!");
        this.containers = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new ContainerInventoryListener(this), plugin);
    }

    public boolean register(UI<?> ui) {
        Preconditions.checkNotNull(ui, "UI object cannot be null!");
        UI<?> oldView = this.containers.put(ui.getUniqueId(), ui);
        if (oldView != null) {
            oldView.close();
        }
        return true;
    }

    public boolean unregister(UI<?> ui) {
        Preconditions.checkNotNull(ui, "UI object cannot be null!");
        UI<?> view = this.containers.remove(ui.getUniqueId());
        if (view == null) return false;
        return view.close();
    }

    @Nullable
    public UI<?> getUI(UUID uuid) {
        Preconditions.checkNotNull(uuid, "UUID object cannot be null!");
        return this.containers.get(uuid);
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
