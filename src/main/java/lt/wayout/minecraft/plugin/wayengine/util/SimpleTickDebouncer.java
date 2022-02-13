package lt.wayout.minecraft.plugin.wayengine.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class SimpleTickDebouncer {
    private final Server server;
    private final int delay;
    private final Runnable runnable;
    private volatile int nextTick;

    public SimpleTickDebouncer(Server server, Runnable runnable, int delayInTicks) {
        this.server = server;
        this.delay = delayInTicks;
        this.runnable = runnable;
        this.nextTick = server.getCurrentTick();
    }

    public int call() {
        int currentTick = Bukkit.getServer().getCurrentTick();
        if (currentTick < this.nextTick) return this.nextTick - currentTick;
        this.nextTick = currentTick + this.delay;
        this.runnable.run();
        return 0;
    }

    public int getDebounceTicks() {
        return this.delay;
    }

    public int getRemainingTicks() {
        return Math.max(0, this.nextTick - this.server.getCurrentTick());
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public Server getServer() {
        return server;
    }

}
