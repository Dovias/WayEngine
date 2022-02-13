package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

import lt.wayout.minecraft.plugin.wayengine.ui.container.GUIContainerView;
import lt.wayout.minecraft.plugin.wayengine.ui.container.ServerGUIContainerView;
import org.bukkit.entity.Player;

import java.util.Collection;

public class GUIActionContext {
    private final GUIContainerView view;
    private final Player player;
    private final Collection<Integer> slots;
    private final GUIContainerAction action;

    public GUIActionContext(GUIContainerView view, Player player, Collection<Integer> rawSlots, GUIContainerAction action) {
        this.view = view;
        this.player = player;
        this.slots = rawSlots;
        this.action = action;

    }

    public GUIContainerAction getAction() {
        return this.action;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Collection<Integer> getRawSlots() {
        return this.slots;
    }

    public GUIContainerView getView() {
        return this.view;
    }


}
