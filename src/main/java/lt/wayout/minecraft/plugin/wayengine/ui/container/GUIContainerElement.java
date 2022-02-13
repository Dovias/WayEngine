package lt.wayout.minecraft.plugin.wayengine.ui.container;

import org.bukkit.entity.Player;

public abstract class GUIContainerElement implements UIContainerElement<GUIContainerView> {

    public boolean testPlayer(Player player) {
        return true;
    }
}
