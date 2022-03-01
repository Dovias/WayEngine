package lt.wayout.minecraft.plugin.wayengine.util;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

public class EventExecutorListener <E extends Event> implements Listener, EventExecutor {
    final Consumer<? super E> consumer;

    public EventExecutorListener(Consumer<? super E> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        consumer.accept((E)event);
    }

    public static <E extends Event> void register(Plugin plugin, Class<E> clazz, Consumer<? super E> listener) {
        EventExecutorListener<E> e = new EventExecutorListener<E>(listener);
        plugin.getServer().getPluginManager().registerEvent(clazz, e, EventPriority.NORMAL, e, plugin,false);
    }
}