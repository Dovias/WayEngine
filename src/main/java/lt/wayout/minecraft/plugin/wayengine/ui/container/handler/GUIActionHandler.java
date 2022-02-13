package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

public interface GUIActionHandler {

    boolean handleItems(GUIActionContext context);

    boolean handleItemAnimations(GUIActionContext context);
}
