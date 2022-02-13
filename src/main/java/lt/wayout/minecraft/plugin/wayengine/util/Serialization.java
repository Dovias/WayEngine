package lt.wayout.minecraft.plugin.wayengine.util;

import net.minecraft.nbt.*;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Serialization {

    private Serialization() {}

    @NotNull
    public static List<Map<String, Object>> serialize(@NotNull final List<ItemStack> itemStacks, boolean dataTypes) {
        List<Map<String, Object>> data = new ArrayList<>(itemStacks.size());
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) continue;
            data.add(Serialization.serialize(itemStack, dataTypes));
        }
        return data;
    }

    @NotNull
    public static List<Map<String, Object>> serialize(@NotNull final ItemStack[] itemStacks, boolean dataTypes) {
        List<Map<String, Object>> data = new ArrayList<>(itemStacks.length);
        for (ItemStack itemStack : itemStacks) {
            data.add(Serialization.serialize(itemStack, dataTypes));
        }
        return data;
    }

    @NotNull
    public static Map<String, Object> serialize(@NotNull final ItemStack itemStack, boolean dataTypes) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", itemStack.getType().getKey().asString());
        data.put("amount", itemStack.getAmount());
        CompoundTag compoundTag = CraftItemStack.asNMSCopy(itemStack).getTag();
        if (compoundTag == null) return data;
        Object serializedData = Serialization.serialize(compoundTag, dataTypes);
        if (!(serializedData instanceof Map<?, ?> mapData) || mapData.isEmpty()) return data;
        data.put("data", mapData);
        return data;
    }

    // Ugly. Needs something more fancy and cleaner looking.
    @Nullable
    public static Object serialize(@NotNull final Tag tag, boolean dataTypes) {
        // VERY UGLY! I CAN'T STAND LOOKING AT THIS.
        if (tag instanceof CompoundTag cTag) {
            Map<String, Object> dataMap = new HashMap<>();
            for (Map.Entry<String, Tag> entry : cTag.tags.entrySet()) {
                dataMap.put(entry.getKey(), Serialization.serialize(entry.getValue(), dataTypes));
            }
            return dataMap;
        } else if (tag instanceof CollectionTag<?> cTag) {
            List<Object> list = new ArrayList<>(cTag.size());
            for (int i = 0; i < cTag.size(); i++) {
                Tag elementTag = cTag.get(i);
                list.add(Serialization.serialize(elementTag, false));
            }
            if (dataTypes && !(cTag instanceof ListTag)) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("type", cTag.getType().getName().substring(0, cTag.getType().getName().length()-2).toLowerCase(Locale.ROOT) + "_array");
                map.put("values", list);
                return map;
            }
            return list;
        } else if (tag instanceof StringTag sTag) {
            return sTag.getAsString();
        } else if (tag instanceof IntTag iTag) {
            return iTag.getAsInt();
        } else if (tag instanceof NumericTag nData) {
            if (!dataTypes) return nData.getAsNumber();
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", nData.getType().getName().toLowerCase(Locale.ROOT));
            map.put("value", nData.getAsNumber());
            return map;
        }
        return null;
    }

    @NotNull
    public static List<ItemStack> deserialize(@NotNull final List<Map<?, ?>> serializedData) {
        List<ItemStack> items = new ArrayList<>();
        for (Map<?, ?> serializedMap : serializedData) {
            ItemStack serializedStack = Serialization.deserialize(serializedMap);
            if (serializedStack != null) items.add(serializedStack);
        }
        return items;
    }

    @Nullable
    public static ItemStack deserialize(@NotNull final Map<?, ?> serializedData) {
        if (!(serializedData.get("type") instanceof String type) || !(serializedData.get("amount") instanceof Integer amount)) return null;
        Material material = Material.matchMaterial(type);
        if (material == null) return null;
        net.minecraft.world.item.ItemStack nmsItemStack = new net.minecraft.world.item.ItemStack(CraftMagicNumbers.getItem(material), amount);
        if (!(serializedData.get("data") instanceof Map<?, ?> serializedNbtMap) || !(Serialization.deserialize((Object)serializedNbtMap) instanceof CompoundTag compoundTag)) return CraftItemStack.asBukkitCopy(nmsItemStack);
        nmsItemStack.setTag(compoundTag);
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    @Nullable
    public static Tag deserialize(@NotNull final Object serializedData) {
        if (serializedData instanceof Map<?, ?> dataMap) {
            if (dataMap.get("type") instanceof String key) {
                Object value = dataMap.get("value");
                if (value == null) value = dataMap.get("values");
                if (value != null) {
                    // VERY UGLY! I CAN'T STAND LOOKING AT THIS!!!!
                    if (value instanceof Number number) {
                        if (key.equalsIgnoreCase("double")) {
                            return DoubleTag.valueOf(number.doubleValue());
                        } else if (key.equalsIgnoreCase("float")) {
                            return FloatTag.valueOf(number.floatValue());
                        } else if (key.equalsIgnoreCase("long")) {
                            return LongTag.valueOf(number.longValue());
                        } else if (key.equalsIgnoreCase("int")) {
                            return IntTag.valueOf(number.intValue());
                        } else if (key.equalsIgnoreCase("short")) {
                            return ShortTag.valueOf(number.shortValue());
                        } else if (key.equalsIgnoreCase("byte")) {
                            return ByteTag.valueOf(number.byteValue());
                        }
                    } else if (value instanceof List<?> list) {
                        // Kinda breaks DRY principle.
                        if (key.equalsIgnoreCase("list")) {
                            return Serialization.deserialize(serializedData);
                        } else if (key.equalsIgnoreCase("long_array")) {
                            long[] array = new long[list.size()];
                            if (list.get(0) instanceof Number) {
                                for (int i = 0; i < array.length; i++) {
                                    array[i] = ((Number) list.get(i)).longValue();
                                }
                            }
                            return new LongArrayTag(array);
                        } else if (key.equalsIgnoreCase("int_array")) {
                            int[] array = new int[list.size()];
                            if (list.get(0) instanceof Number) {
                                for (int i = 0; i < array.length; i++) {
                                    array[i] = ((Number) list.get(i)).intValue();
                                }
                            }
                            return new IntArrayTag(array);
                        } else if (key.equalsIgnoreCase("byte_array")) {
                            byte[] array = new byte[list.size()];
                            if (list.get(0) instanceof Number) {
                                for (int i = 0; i < array.length; i++) {
                                    array[i] = ((Number) list.get(i)).byteValue();
                                }
                            }
                            return new ByteArrayTag(array);
                        }
                    }
                }
            }
            CompoundTag compoundTag = new CompoundTag();

            for (Map.Entry<?, ?> entry : dataMap.entrySet()) {
                if (!(entry.getKey() instanceof String key)) continue;
                Tag tag = Serialization.deserialize(entry.getValue());
                if (tag == null) continue;
                compoundTag.put(key, tag);
            }
            return compoundTag;
        }
        else if (serializedData instanceof List<?> listData) {
            ListTag listTag = new ListTag();
            for (int i = 0; i < listData.size(); i++) {
                Tag tag = Serialization.deserialize(listData.get(i));
                if (tag == null) continue;
                listTag.add(i, tag);
            }
            return listTag;
        }
        else if (serializedData instanceof Double doubleData) return DoubleTag.valueOf(doubleData);
        else if (serializedData instanceof Float floatData) return FloatTag.valueOf(floatData);
        else if (serializedData instanceof Long longData) return LongTag.valueOf(longData);
        else if (serializedData instanceof Integer intData) return IntTag.valueOf(intData);
        else if (serializedData instanceof Short shortData) return ShortTag.valueOf(shortData);
        else if (serializedData instanceof Byte byteData) return ByteTag.valueOf(byteData);
        else if (serializedData instanceof String stringData) return StringTag.valueOf(stringData);
        else return null;

    }
}
