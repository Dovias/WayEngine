package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GUIAnimatedContainerItem extends GUIContainerItem {
    private final List<ItemStack> keyframeStacks;
    private final int speed;
    private final boolean repeat;
    private boolean playing, paused;

    public GUIAnimatedContainerItem(ItemStack itemStack, Collection<ItemStack> keyFrameStacks, int speed, boolean repeat) {
        super(itemStack);
        this.keyframeStacks = keyFrameStacks == null ? Collections.emptyList() : Arrays.asList(keyFrameStacks.toArray(new ItemStack[0]));
        this.speed = Math.max(1, speed);
        this.repeat = repeat;
    }

    public void play() {
        this.playing = true;
        this.paused = false;
    }

    public void stop() {
        this.playing = false;
        this.paused = false;
    }

    public void pause() {
        if (this.playing) this.paused = true;
    }

    public void unpause() {
        this.paused = false;
    }

    public List<ItemStack> getKeyframeStacks() {
        return Collections.unmodifiableList(this.keyframeStacks);
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isOnRepeat() {
        return this.repeat;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public boolean isPaused() {
        return this.paused;
    }
}
