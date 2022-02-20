package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.base.Preconditions;

import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

public abstract class GUIContainerView implements UIContainerView<GUIContainer> {
    private final GUIContainer container;
    private final GUIContainerElement[] elements;
    private final boolean isTransient;

    public GUIContainerView(@NotNull final GUIContainer container, final boolean isTransient) {
        this.container = container;
        this.elements = new GUIContainerElement[this.container.getSize() + ContainerType.PLAYER.getSize()-5];
        for (int i = 0; i < this.container.getSize(); i++) {
            this.elements[i] = this.container.getElement(i);
        }
        this.isTransient = isTransient;
    }

    public GUIContainerView(@NotNull final GUIContainer container) {
        this(container, true);
    }

    @Override
    public @NotNull GUIContainer getUI() {
        return this.container;
    }

    @NotNull
    public ContainerType getType() {
        return this.getUI().getType();
    }

    public int getSize() {
        return this.elements.length;
    }

    public void setElement(@Nullable final GUIContainerElement element, final int slot) {
        Preconditions.checkPositionIndex(slot, this.getSize(), "Slot index cannot be less than 0 or bigger than container view size!");
        this.elements[slot] = element;
    }

    public GUIContainerElement getElement(final int slot) {
        Preconditions.checkPositionIndex(slot, this.getSize(), "Slot index cannot be less than 0 or bigger than container view size!");
        return this.elements[slot];
    }

    public boolean hasElement(final int slot) {
        Preconditions.checkPositionIndex(slot, this.getSize(), "Slot index cannot be less than 0 or bigger than container size!");
        return this.elements[slot] != null;
    }

    @NotNull
    public Collection<GUIContainerElement> getElements() {
        return Arrays.asList(this.elements);
    }

    public boolean isTransient() {
        return this.isTransient;
    }
}
