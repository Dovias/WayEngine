package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.primitives.Ints;

import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionHandler;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerGUIContainer extends GUIContainer {
    private final UUID uuid;
    private final Map<Player, GUIContainerView> views;
    private final Inventory inventory;


    public ServerGUIContainer(@NotNull Plugin plugin, @NotNull ContainerType type, @Nullable String name, GUIActionHandler handler, List<? extends GUIContainerElement> elements) {
        super(plugin, type, name, handler, elements);

        Server server = super.getPlugin().getServer();
        this.inventory = switch(super.getType()) {
            case GENERIC_9X1, GENERIC_9X2, GENERIC_9X3, GENERIC_9X4, GENERIC_9X5, GENERIC_9X6 -> server.createInventory(null, super.getSize(), super.getDisplayName());
            default -> server.createInventory(null, super.getType().toBukkit(), super.getDisplayName());
        };
        for (int i = 0; i < super.getSize(); i++) {
            if (!(super.getElement(i) instanceof GUIContainerItem containerItem)) continue;
            this.inventory.setItem(i, containerItem.getItemStack());
        }

        this.uuid = UUID.nameUUIDFromBytes(Ints.toByteArray(this.inventory.hashCode()));
        this.views = new IdentityHashMap<>();
    }

    @Override
    public boolean open(@NotNull final Player player, @NotNull final GUIContainerView view) {
        //return this.views.computeIfAbsent(player, (key) -> player.openInventory(this.inventory) != null && view.open(key) ? view : null) == view;
        return view.getUI() == this && !this.views.containsKey(player) && this.views.put(player, view) == null && player.openInventory(this.inventory) != null && view.open(player);

    }

    @Override
    public @Nullable GUIContainerView open(@NotNull final Player player) {
        GUIContainerView view = new ServerGUIContainerView(this);
        return this.open(player, view) ? view : null;
    }

    @Override
    public boolean close(@NotNull final Player player) {
        if (this.views.remove(player) == null) return false;
        if (player.getOpenInventory().getTopInventory() == this.inventory) {
            player.closeInventory();
        }
        return true;
    }

    @Override
    public boolean close() {
        for (Iterator<Player> iterator = this.views.keySet().iterator(); iterator.hasNext();) {
            Player player = iterator.next();
            if (player.getOpenInventory().getTopInventory() == this.inventory) {
                player.closeInventory();
            }
            iterator.remove();
        }
        return true;
    }

    @Override
    public @Nullable GUIContainerView getView(@NotNull Player player) {
        return this.views.get(player);
    }

    @Override
    public boolean hasView(@NotNull Player player) {
        return this.views.containsKey(player);
    }

    @Override
    public @NotNull Collection<Player> getViewers() {
        return Collections.unmodifiableCollection(this.views.keySet());
    }

    @Override
    public @NotNull Collection<GUIContainerView> getViews() {
        return Collections.unmodifiableCollection(this.views.values());
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }
}
