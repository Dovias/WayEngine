package lt.wayout.minecraft.plugin.wayengine.packet.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PacketItemStack {
    private final ItemStack itemStack;

    public PacketItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = Preconditions.checkNotNull(itemStack, "ItemStack object cannot be null!");
    }

    @NotNull
    public ItemStack getBukkitItemStack() {
        return itemStack;
    }

    public void display(InventoryView view, int slot) {

        ClientboundContainerSetSlotPacket packet = new ClientboundContainerSetSlotPacket(((CraftPlayer)view.getPlayer()).getHandle().bV.j, 0, slot, CraftItemStack.asNMSCopy(this.itemStack));
        ((CraftPlayer)view.getPlayer()).getHandle().b.sendPacket(packet);
    }
}
