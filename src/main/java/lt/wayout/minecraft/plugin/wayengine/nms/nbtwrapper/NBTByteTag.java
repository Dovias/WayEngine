package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTByteTag implements NBTTag {
    private final byte data;

    public NBTByteTag(byte data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "byte";
    }

    public Byte getRawValue() {
        return this.data;
    }

}
