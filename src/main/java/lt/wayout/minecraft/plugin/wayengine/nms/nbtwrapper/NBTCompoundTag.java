package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

import java.util.*;

public class NBTCompoundTag implements NBTTag {
    private final Map<String, NBTTag> map = new HashMap<>();

    @Override
    public String getName() {
        return null;
    }

    public NBTTag get(String key) {
        return this.map.get(key);
    }

    public Set<String> getAllKeys() {
        return Collections.unmodifiableSet(this.map.keySet());
    }

    public Collection<NBTTag> getAllValues() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    public void put(String key, NBTTag value) {
        this.map.put(key, value);
    }


}
