package lt.wayout.minecraft.plugin.wayengine.ui.container;

import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionHandler;

import lt.wayout.minecraft.plugin.wayengine.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerGUIContainer extends GUIContainer {
    private final Map<UUID, GUIContainerView> views;
    //private boolean cleaning;


    public ServerGUIContainer(@NotNull Plugin plugin, @NotNull ContainerType type, @Nullable String name, GUIActionHandler handler, List<? extends GUIContainerElement> elements) {
        super(plugin, type, name, handler, elements);
        this.views = new HashMap<>();
    }

    /*@Override
    public boolean open(@NotNull final Player player, @NotNull final GUIContainerView view) {
        return this.views.computeIfAbsent(player, (key) -> player.openInventory(this.inventory) != null && view.open(key) ? view : null) == view;
        return view.getUI() == this && !this.views.containsKey(player) && this.views.put(player, view) == null && view.open(player);
    }*/

    @Override
    public @Nullable GUIContainerView open(@NotNull final Player player) {
        /*GUIContainerView view = new ServerGUIContainerView(this);
        return this.open(player, view) ? view : null;*/
        GUIContainerView view = new ServerGUIContainerView(this);
        return view.open(player) ? this.views.put(view.getUniqueId(), view) : null;
    }

    @Override
    public boolean close(@NotNull final Player player) {
        /*GUIContainerView view = this.views.remove(player);
        return view != null && view.close(player);
         */
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
        /*if (view.getUI() != this || this.cleaning) return false;
        this.cleaning = true;
        for (Iterator<Map.Entry<Player, GUIContainerView>> iterator = this.views.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Player, GUIContainerView> entry = iterator.next();
            if (entry.getValue() != view) continue;
            entry.getValue().close(entry.getKey());
            iterator.remove();
        }
        this.cleaning = false;
        return true;*/
        return view.getUI() == this && this.views.remove(view.getUniqueId(), view) && view.close();
    }

    @Override
    public boolean close() {
        /*this.cleaning = true;
        for (Iterator<GUIContainerView> iterator = this.views.values().iterator(); iterator.hasNext();) {
            GUIContainerView view = iterator.next();
            iterator.remove();
            view.close();
        }
        this.cleaning = false;
        return true;*/
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
