package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nullable;

public enum GUIContainerClickType {
    /**
     * The left (or primary) mouse button.
     */
    LEFT,
    /**
     * Holding shift while pressing the left mouse button.
     */
    SHIFT_LEFT,
    /**
     * The right mouse button.
     */
    RIGHT,
    /**
     * Holding shift while pressing the right mouse button.
     */
    SHIFT_RIGHT,
    /**
     * Clicking the left mouse button on the grey area around the inventory.
     */
    WINDOW_BORDER_LEFT,
    /**
     * Clicking the right mouse button on the grey area around the inventory.
     */
    WINDOW_BORDER_RIGHT,
    /**
     * The middle mouse button, or a "scrollwheel click".
     */
    MIDDLE,
    /**
     * One of the number keys 1-9, correspond to slots on the hotbar.
     */
    NUMBER_KEY,
    /**
     * Pressing the left mouse button twice in quick succession.
     */
    DOUBLE_CLICK,
    /**
     * The "Drop" key (defaults to Q).
     */
    DROP,
    /**
     * Holding Ctrl while pressing the "Drop" key (defaults to Q).
     */
    CONTROL_DROP,
    /**
     * Any action done with the Creative inventory open.
     */
    CREATIVE,
    /**
     * The "swap item with offhand" key (defaults to F).
     */
    SWAP_OFFHAND;


    /**
     * Gets whether this ClickType represents the pressing of a key on a
     * keyboard.
     *
     * @return true if this ClickType represents the pressing of a key
     */
    public boolean isKeyboardClick() {
        return (this == GUIContainerClickType.NUMBER_KEY) || (this == GUIContainerClickType.DROP) || (this == GUIContainerClickType.CONTROL_DROP);
    }

    /**
     * Gets whether this ClickType represents an action that can only be
     * performed by a Player in creative mode.
     *
     * @return true if this action requires Creative mode
     */
    public boolean isCreativeAction() {
        // Why use middle click?
        return (this == GUIContainerClickType.MIDDLE) || (this == GUIContainerClickType.CREATIVE);
    }

    /**
     * Gets whether this ClickType represents a right click.
     *
     * @return true if this ClickType represents a right click
     */
    public boolean isRightClick() {
        return (this == GUIContainerClickType.RIGHT) || (this == GUIContainerClickType.SHIFT_RIGHT);
    }

    /**
     * Gets whether this ClickType represents a left click.
     *
     * @return true if this ClickType represents a left click
     */
    public boolean isLeftClick() {
        return (this == GUIContainerClickType.LEFT) || (this == GUIContainerClickType.SHIFT_LEFT) || (this == GUIContainerClickType.DOUBLE_CLICK) || (this == GUIContainerClickType.CREATIVE);
    }

    /**
     * Gets whether this ClickType indicates that the shift key was pressed
     * down when the click was made.
     *
     * @return true if the action uses Shift.
     */
    public boolean isShiftClick() {
        return (this == GUIContainerClickType.SHIFT_LEFT) || (this == GUIContainerClickType.SHIFT_RIGHT) || (this == GUIContainerClickType.CONTROL_DROP);
    }

    @Nullable
    public static GUIContainerClickType fromBukkit(ClickType type) {
        return switch (type) {
            case LEFT -> GUIContainerClickType.LEFT;
            case SHIFT_LEFT -> GUIContainerClickType.SHIFT_LEFT;
            case RIGHT -> GUIContainerClickType.RIGHT;
            case SHIFT_RIGHT -> GUIContainerClickType.SHIFT_RIGHT;
            case WINDOW_BORDER_LEFT -> GUIContainerClickType.WINDOW_BORDER_LEFT;
            case WINDOW_BORDER_RIGHT -> GUIContainerClickType.WINDOW_BORDER_RIGHT;
            case MIDDLE -> GUIContainerClickType.MIDDLE;
            case NUMBER_KEY -> GUIContainerClickType.NUMBER_KEY;
            case DOUBLE_CLICK -> GUIContainerClickType.DOUBLE_CLICK;
            case DROP -> GUIContainerClickType.DROP;
            case CONTROL_DROP -> GUIContainerClickType.CONTROL_DROP;
            case CREATIVE -> GUIContainerClickType.CREATIVE;
            case SWAP_OFFHAND -> GUIContainerClickType.SWAP_OFFHAND;
            default -> null;
        };
    }
}
