package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTDoubleTag implements NBTTag {
    private final double data;

    public NBTDoubleTag(double data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "double";
    }

    public Double getRawValue() {
        return this.data;
    }

}
