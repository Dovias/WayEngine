package lt.wayout.minecraft.plugin.wayengine.ui.container.handler;

import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public enum GUIContainerAction {
    PICKUP_ALL,
    PICKUP_SOME,
    PICKUP_HALF,
    PICKUP_ONE,
    PLACE_ALL,
    PLACE_SOME,
    PLACE_ONE,
    SWAP_WITH_CURSOR,
    DROP_ALL_CURSOR,
    DROP_ONE_CURSOR,
    DROP_ALL_SLOT,
    DROP_ONE_SLOT,
    MOVE_TO_OTHER_INVENTORY,
    HOTBAR_MOVE_AND_READD,
    HOTBAR_SWAP,
    CLONE_STACK,
    COLLECT_TO_CURSOR,
    DRAG_EVEN,
    DRAG_SINGLE,
    INTERACT_HAND,
    INTERACT_OFF_HAND;

    @Nullable
    public static GUIContainerAction fromBukkit(InventoryAction action) {
        return switch (action) {
            case PICKUP_ALL -> GUIContainerAction.PICKUP_ALL;
            case PICKUP_SOME -> GUIContainerAction.PICKUP_SOME;
            case PICKUP_HALF -> GUIContainerAction.PICKUP_HALF;
            case PICKUP_ONE -> GUIContainerAction.PICKUP_ONE;
            case PLACE_ALL -> GUIContainerAction.PLACE_ALL;
            case PLACE_SOME -> GUIContainerAction.PLACE_SOME;
            case PLACE_ONE -> GUIContainerAction.PLACE_ONE;
            case SWAP_WITH_CURSOR -> GUIContainerAction.SWAP_WITH_CURSOR;
            case DROP_ALL_CURSOR -> GUIContainerAction.DROP_ALL_CURSOR;
            case DROP_ONE_CURSOR -> GUIContainerAction.DROP_ONE_CURSOR;
            case DROP_ALL_SLOT -> GUIContainerAction.DROP_ALL_SLOT;
            case DROP_ONE_SLOT -> GUIContainerAction.DROP_ONE_SLOT;
            case MOVE_TO_OTHER_INVENTORY -> GUIContainerAction.MOVE_TO_OTHER_INVENTORY;
            case HOTBAR_MOVE_AND_READD -> GUIContainerAction.HOTBAR_MOVE_AND_READD;
            case HOTBAR_SWAP -> GUIContainerAction.HOTBAR_SWAP;
            case CLONE_STACK -> GUIContainerAction.CLONE_STACK;
            case COLLECT_TO_CURSOR -> GUIContainerAction.COLLECT_TO_CURSOR;
            default -> null;
        };
    }

    @Nullable
    public static GUIContainerAction fromBukkit(DragType type) {
        if (type == DragType.EVEN) {
            return GUIContainerAction.DRAG_EVEN;
        } else if (type == DragType.SINGLE) {
            return GUIContainerAction.DRAG_SINGLE;
        }
        return null;
    }

    @Nullable
    public static GUIContainerAction fromBukkit(EquipmentSlot type) {
        if (type == EquipmentSlot.HAND) {
            return GUIContainerAction.INTERACT_HAND;
        } else if (type == EquipmentSlot.OFF_HAND) {
            return GUIContainerAction.INTERACT_OFF_HAND;
        }
        return null;
    }
}
