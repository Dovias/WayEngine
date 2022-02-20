package lt.wayout.minecraft.plugin.wayengine.storage;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.thread.SingleThreadPool;

import lt.wayout.minecraft.plugin.wayengine.util.FileUtils;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ItemStackBuffer {
    private final ItemStack[] itemStacks;
    private int amount;

    public ItemStackBuffer(final int size) {
        if (size < 1) {
            throw new IndexOutOfBoundsException("Bukkit itemstack file buffer size must be bigger than 0!");
        }
        this.itemStacks = new ItemStack[size];
    }

    public ItemStackBuffer(ItemStack[] data) {
        this.itemStacks = new ItemStack[data.length];
        for (int i = 0; i < this.itemStacks.length; i++) {
            if (data[i] == null) continue;
            amount++;
            this.itemStacks[i] = data[i];
        }
    }

    public void setItem(final int slot, @Nullable final ItemStack itemStack) {
        if (itemStack != null) {
            amount++;
        } else if (this.itemStacks[slot] != null) {
            amount = Math.max(0, amount - 1);
        }
        this.itemStacks[slot] = itemStack;
    }

    @Nullable
    public ItemStack getItem(final int slot) {
        Preconditions.checkElementIndex(slot, this.getSize());
        return this.itemStacks[slot];
    }

    public int getSize() {
        return this.itemStacks.length;
    }

    public int getAmount() {
        return this.amount;
    }

    public static CompletableFuture<ItemStackBuffer> fromFile(Path path, final boolean deleteFile) {
        return CompletableFuture.supplyAsync(() -> {
            ItemStack[] itemStacks = FileUtils.loadObject(path, ItemStack[].class, deleteFile);
            return itemStacks == null ? null : new ItemStackBuffer(itemStacks);
        }, SingleThreadPool.getInstance());
    }

    public CompletableFuture<Boolean> flushToFile(Path path) {
        return CompletableFuture.supplyAsync(() -> FileUtils.saveObject(path, ItemStackBuffer.this.itemStacks), SingleThreadPool.getInstance());
    }

    public static ItemStackBuffer fromInventory(@NotNull final Inventory inventory) {
        return new ItemStackBuffer(inventory.getContents());

    }

    public void flushToInventory(@NotNull final Inventory inventory) {
        Preconditions.checkArgument(inventory.getSize() <= this.itemStacks.length, "Bukkit inventory size must be the less or the same as bukkit itemstack file buffer size!");
        for (int i = 0; i < this.itemStacks.length; i++) {
            if (itemStacks[i] == null) continue;
            inventory.setItem(i, this.getItem(i));
        }
    }
}
