package lt.wayout.minecraft.plugin.wayengine.ui.container;

import lt.wayout.minecraft.plugin.wayengine.storage.ItemStackBuffer;

import com.google.common.primitives.Ints;

import lt.wayout.minecraft.plugin.wayengine.thread.SingleThreadPool;
import lt.wayout.minecraft.plugin.wayengine.util.FileUtils;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.*;


public class ServerGUIContainerView extends GUIContainerView {
    private final Map<Player, ItemStackBuffer> viewers = new IdentityHashMap<>();
    private final Inventory inventory;
    private final UUID uuid;

    ServerGUIContainerView(@NotNull ServerGUIContainer container, final boolean isTransient) {
        super(container, isTransient);

        Server server = container.getPlugin().getServer();
        this.inventory = switch(super.getType()) {
            case GENERIC_9X1, GENERIC_9X2, GENERIC_9X3, GENERIC_9X4, GENERIC_9X5, GENERIC_9X6 -> server.createInventory(null, super.getUI().getSize(), container.getDisplayName());
            default -> server.createInventory(null, super.getType().toBukkit(), container.getDisplayName());
        };
        for (int i = 0; i < super.getSize(); i++) {
            if (!(super.getElement(i) instanceof GUIContainerItem containerItem)) continue;
            this.inventory.setItem(i, containerItem.getItemStack());
        }
        this.uuid = UUID.nameUUIDFromBytes(Ints.toByteArray(this.inventory.hashCode()));
    }

    @Override
    public boolean open(@NotNull Player player) {
        if (this.viewers.containsKey(player) || !super.getUI().hasView(this)) return false;

        if (!this.isChanged()) {
            this.viewers.put(player, null);
            return true;
        }
        ItemStackBuffer storage = new ItemStackBuffer(player.getInventory().getSize());
        this.viewers.put(player, storage);

        InventoryView bukkitView = player.getOpenInventory();
        for (int i = super.getUI().getSize(); i < super.getSize(); i++) {
            if (!(super.getElement(i) instanceof GUIContainerItem containerItem)) continue;
            storage.setItem(i, bukkitView.getItem(i));
            bukkitView.setItem(i, containerItem.getItemStack());
        }
        storage.flushToFile(Paths.get(super.getUI().getPlugin().getDataFolder().getAbsolutePath(), "invdata", player.getUniqueId() + ".dat"));
        return true;
    }

    @Override
    public boolean close(@NotNull Player player) {
        if (!this.viewers.containsKey(player)) return false;
        ItemStackBuffer storage = this.viewers.remove(player);
        if (this.viewers.isEmpty() && super.isTransient()) {
            super.getUI().close(this);
        }
        if (player.getOpenInventory().getTopInventory() == this.inventory) {
            player.closeInventory();
        }

        //if (!this.isChanged()) return true;
        if (storage == null) return true; // Faster check, since isChanged() uses iterator.
        storage.flushToInventory(player.getInventory());
        SingleThreadPool.getInstance().submit(() -> FileUtils.deleteFile(Paths.get(super.getUI().getPlugin().getDataFolder().getAbsolutePath(), "invdata", player.getUniqueId() + ".dat")));
        return true;
    }

    @Override
    public boolean close() {
        for (Iterator<Map.Entry<Player, ItemStackBuffer>> iterator = this.viewers.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Player, ItemStackBuffer> entry = iterator.next();
            iterator.remove();

            Player player = entry.getKey();
            if (player.getOpenInventory().getTopInventory() == this.inventory) {
                player.closeInventory();
            }

            ItemStackBuffer storage = entry.getValue();
            //if (!this.isChanged()) continue;
            if (storage == null) continue; // Faster check, since isChanged() uses iterator.
            storage.flushToInventory(player.getInventory());
            SingleThreadPool.getInstance().submit(() -> FileUtils.deleteFile(Paths.get(super.getUI().getPlugin().getDataFolder().getAbsolutePath(), "invdata", player.getUniqueId() + ".dat")));
        }
        if (super.isTransient()) {
            super.getUI().close(this);
        }
        return true;
    }

    @Override
    public void setElement(@Nullable GUIContainerElement element, int slot) {
        GUIContainerElement prevElement = super.getElement(slot);
        if (prevElement != null && element == prevElement) return;

        super.setElement(element, slot);
        for (Map.Entry<Player, ItemStackBuffer> entry : this.viewers.entrySet()) {
            Player player = entry.getKey();
            InventoryView bukkitView = player.getOpenInventory();
            if (bukkitView.getTopInventory() != this.inventory) continue;
            if (slot < super.getUI().getSize()) {
                bukkitView.setItem(slot, !(element instanceof GUIContainerItem containerItem) ? null : containerItem.getItemStack());
                continue;
            }
            ItemStackBuffer storage = entry.getValue();

            int convertedSlot = bukkitView.convertSlot(slot);
            if (element instanceof GUIContainerItem containerItem) {
                if (storage == null) {
                    ItemStackBuffer.fromInventory(player.getInventory()).flushToFile(Paths.get(super.getUI().getPlugin().getDataFolder().getAbsolutePath(), "invdata", player.getUniqueId() + ".dat"));
                    storage = new ItemStackBuffer(player.getInventory().getSize());
                }
                ItemStack itemStack = bukkitView.getItem(slot);
                if (itemStack == null) {
                    itemStack = new ItemStack(Material.AIR);
                }
                storage.setItem(convertedSlot, itemStack);

                bukkitView.setItem(slot, containerItem.getItemStack());

                entry.setValue(storage);
                continue;
            }

            if (storage != null) {
                if (storage.getAmount() == 1) {
                    entry.setValue(null);
                    /* Would cause IO issues.
                    SingleThreadPool.getInstance().submit(() -> FileUtils.deleteFile(Paths.get(super.getUI().getPlugin().getDataFolder().getAbsolutePath(), "invdata", player.getUniqueId() + ".dat")));
                    */
                }
                ItemStack itemStack = storage.getItem(convertedSlot);
                storage.setItem(convertedSlot, null);
                bukkitView.setItem(slot, itemStack);
            }
        }
    }

    private boolean isChanged() {
        Iterator<ItemStackBuffer> iterator = this.viewers.values().iterator();
        return iterator.hasNext() && iterator.next() != null;
    }

    @Override
    public boolean isViewing(@NotNull Player player) {
        return this.viewers.containsKey(player);
    }

    @Override
    public @NotNull Collection<Player> getViewers() {
        return Collections.unmodifiableCollection(this.viewers.keySet());
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }
}
