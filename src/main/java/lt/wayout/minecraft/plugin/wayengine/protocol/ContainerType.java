package lt.wayout.minecraft.plugin.wayengine.protocol;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum ContainerType {
    GENERIC_3X3(0, 9),
    GENERIC_5X1(1, 5),
    GENERIC_9X1(2, 9),
    GENERIC_9X2(3, 18),
    GENERIC_9X3(4, 27),
    GENERIC_9X4(5, 36),
    GENERIC_9X5(6, 45),
    GENERIC_9X6(7, 54),
    ANVIL(8, 3),
    BEACON(9, 1),
    BLAST_FURNACE(10, 3),
    BREWING_STAND(11, 5),
    CRAFTING_TABLE(12, 10),
    ENCHANTING_TABLE(13, 2),
    FURNACE(14, 3),
    GRINDSTONE(15, 3),
    LECTERN(16, 1),
    LOOM(17, 4),
    MERCHANT(18, 3),
    SHULKER_BOX(19, 27),
    SMITHING_TABLE(20, 3),
    SMOKER(21, 3),
    CARTOGRAPHY_TABLE(22, 3),
    STONECUTTER(23, 2),
    PLAYER(41),
    CRAFTING(5);

    private final int id, size;
    private static final Map<Integer, ContainerType> byId = new HashMap<>();
    static {
        for (ContainerType type : ContainerType.values()) {
            if (!type.isCreatable()) continue;
            if (byId.put(type.getProtocolId(), type) != null) {
                throw new IllegalArgumentException("Duplicate container type protocol id: " + type.getProtocolId() + '!');
            }
        }
    }

    ContainerType(int protocolId, int size) {
        this.id = protocolId;
        this.size = size;
    }

    ContainerType(int size) {
        this.id = -1;
        this.size = size;
    }

    public int getProtocolId() {
        if (this.id == -1) {
            throw new NullPointerException("This container type does not have protocol id!");
        }
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isCreatable() {
        return this.id != -1;
    }

    @NotNull
    public InventoryType toBukkit() {
        return ContainerType.toBukkit(this);
    }

    @NotNull
    public static InventoryType toBukkit(ContainerType type) {
        return switch (type) {
            case GENERIC_3X3 -> InventoryType.DROPPER;
            case GENERIC_5X1 -> InventoryType.HOPPER;
            case GENERIC_9X1, GENERIC_9X5, GENERIC_9X3, GENERIC_9X4, GENERIC_9X2, GENERIC_9X6 -> InventoryType.CHEST;
            case ANVIL -> InventoryType.ANVIL;
            case BEACON -> InventoryType.BEACON;
            case BLAST_FURNACE -> InventoryType.BLAST_FURNACE;
            case BREWING_STAND -> InventoryType.BREWING;
            case CRAFTING_TABLE -> InventoryType.WORKBENCH;
            case ENCHANTING_TABLE -> InventoryType.ENCHANTING;
            case FURNACE -> InventoryType.FURNACE;
            case GRINDSTONE -> InventoryType.GRINDSTONE;
            case LECTERN -> InventoryType.LECTERN;
            case LOOM -> InventoryType.LOOM;
            case MERCHANT -> InventoryType.MERCHANT;
            case SHULKER_BOX -> InventoryType.SHULKER_BOX;
            case SMITHING_TABLE -> InventoryType.SMITHING;
            case SMOKER -> InventoryType.SMOKER;
            case CARTOGRAPHY_TABLE -> InventoryType.CARTOGRAPHY;
            case STONECUTTER -> InventoryType.STONECUTTER;
            case PLAYER -> InventoryType.PLAYER;
            case CRAFTING -> InventoryType.CRAFTING;
        };
    }

    @Nullable
    public static ContainerType fromBukkit(InventoryType type) {
        return switch (type) {
            case DROPPER, DISPENSER -> ContainerType.GENERIC_3X3;
            case HOPPER -> ContainerType.GENERIC_5X1;
            case CHEST, BARREL, ENDER_CHEST -> ContainerType.GENERIC_9X3;
            case ANVIL -> ContainerType.ANVIL;
            case BEACON -> ContainerType.BEACON;
            case BLAST_FURNACE -> ContainerType.BLAST_FURNACE;
            case BREWING -> ContainerType.BREWING_STAND;
            case WORKBENCH -> ContainerType.CRAFTING_TABLE;
            case ENCHANTING -> ContainerType.ENCHANTING_TABLE;
            case FURNACE -> ContainerType.FURNACE;
            case GRINDSTONE -> ContainerType.GRINDSTONE;
            case LECTERN -> ContainerType.LECTERN;
            case LOOM -> ContainerType.LOOM;
            case MERCHANT -> ContainerType.MERCHANT;
            case SHULKER_BOX -> ContainerType.SHULKER_BOX;
            case SMITHING -> ContainerType.SMITHING_TABLE;
            case SMOKER -> ContainerType.SMOKER;
            case CARTOGRAPHY -> ContainerType.CARTOGRAPHY_TABLE;
            case STONECUTTER -> ContainerType.STONECUTTER;
            case PLAYER -> ContainerType.PLAYER;
            case CRAFTING -> ContainerType.CRAFTING;
            default -> null;
        };
    }

    @NotNull
    public static ContainerType fromProtocolId(int protocolId) {
        return ContainerType.byId.get(protocolId);
    }
}
