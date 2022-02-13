package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTShortTag implements NBTTag {
    private final short data;

    public NBTShortTag(short data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "short";
    }

    public Short getRawValue() {
        return this.data;
    }

}
