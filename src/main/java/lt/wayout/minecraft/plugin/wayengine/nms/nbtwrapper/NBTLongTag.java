package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTLongTag implements NBTTag {
    private final long data;

    public NBTLongTag(long data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "long";
    }

    public Long getRawValue() {
        return this.data;
    }

}
