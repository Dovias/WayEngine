package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.base.Preconditions;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ServerGUIContainerView extends GUIContainerView {

    public ServerGUIContainerView(@NotNull ServerGUIContainer container) {
        super(container);
    }

    @Override
    public boolean open(@NotNull Player player) {
        if (!this.hasView(player)) {
            return super.getUI().open(player, this);
        }
        InventoryView bukkitView = player.getOpenInventory();
        for (int i = 0; i < super.getSize(); i++) {
            if (!(super.getElement(i) instanceof GUIContainerItem containerItem)) continue;
            bukkitView.setItem(i, containerItem.getItemStack());
        }
        return true;
    }

    @Override
    public boolean close(@NotNull Player player) {
        return super.getUI().close(player);
    }

    @Override
    public boolean close() {
        return super.getUI().close();
    }

    public boolean hasView(@NotNull Player player) {
        return super.getUI().hasView(player);
    }
}
