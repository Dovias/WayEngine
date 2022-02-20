package lt.wayout.minecraft.plugin.wayengine.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SingleThreadPool {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private SingleThreadPool() {}

    public static ExecutorService getInstance() {
        return SingleThreadPool.executor;
    }
}
