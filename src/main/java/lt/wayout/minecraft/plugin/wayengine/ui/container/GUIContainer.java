package lt.wayout.minecraft.plugin.wayengine.ui.container;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIActionHandler;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GUIContainer implements UIContainer<GUIContainerView> {
    private final Plugin plugin;
    private final ContainerType type;
    private final String name;
    private final GUIActionHandler handler;
    private final GUIContainerElement[] elements;

    public GUIContainer(@NotNull final Plugin plugin, @NotNull final ContainerType type, @Nullable final String displayName, @NotNull final GUIActionHandler handler, @NotNull final List<? extends GUIContainerElement> elements) {
        this.plugin = plugin;
        this.type = type;
        this.name = displayName == null ? "" : displayName;
        this.handler = handler;
        this.elements = new GUIContainerElement[type.getSize()];
        int length = Math.min(elements.size(), this.elements.length);
        Iterator<? extends GUIContainerElement> iterator = elements.iterator();
        for (int i = 0; i < length; i++) {
            this.elements[i] = iterator.next();
        }
    }

    @Override
    public int getSize() {
        return this.type.getSize();
    }

    @NotNull
    public GUIActionHandler getHandler() {
        return this.handler;
    }

    @NotNull
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public @NotNull Collection<GUIContainerElement> getElements() {
        return Arrays.asList(this.elements);
    }

    @Nullable
    public GUIContainerElement getElement(int slot) {
        Preconditions.checkElementIndex(slot, this.elements.length);
        return this.elements[slot];
    }

    public boolean hasElement(int slot) {
        Preconditions.checkElementIndex(slot, this.elements.length);
        return this.elements[slot] != null;
    }

    @NotNull
    public ContainerType getType() {
        return this.type;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

}
