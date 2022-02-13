package lt.wayout.minecraft.plugin.wayengine.nms.nbtwrapper;

public class NBTIntTag implements NBTTag {
    private final int data;

    public NBTIntTag(int data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "int";
    }

    public Integer getRawValue() {
        return this.data;
    }

}
