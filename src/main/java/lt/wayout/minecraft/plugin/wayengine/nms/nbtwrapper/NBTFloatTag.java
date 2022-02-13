package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTFloatTag implements NBTTag {
    private final float data;

    public NBTFloatTag(float data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "float";
    }

    public Float getRawValue() {
        return this.data;
    }

}
