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

    public GUIContainerView(@NotNull final GUIContainer container) {
        this.container = container;
        this.elements = new GUIContainerElement[this.container.getSize() + ContainerType.PLAYER.getSize()-5];
        for (int i = 0; i < this.container.getSize(); i++) {
            this.elements[i] = this.container.getElement(i);
        }
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
        elements[slot] = element;
    }

    public GUIContainerElement getElement(final int slot) {
        Preconditions.checkPositionIndex(slot, this.getSize(), "Slot index cannot be less than 0 or bigger than container view size!");
        return this.elements[slot];
    }

    public boolean hasElement(final int slot) {
        Preconditions.checkPositionIndex(slot, this.getSize(), "Slot index cannot be less than 0 or bigger than container size!");
        return elements[slot] != null;
    }

    @NotNull
    public Collection<GUIContainerElement> getElements() {
        return Arrays.asList(this.elements);
    }

}
