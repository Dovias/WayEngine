package lt.wayout.minecraft.plugin.wayengine.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPool {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private SingleThreadPool() {}

    public static ExecutorService getInstance() {
        return SingleThreadPool.executor;
    }
}
