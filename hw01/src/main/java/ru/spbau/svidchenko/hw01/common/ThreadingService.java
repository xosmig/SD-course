package ru.spbau.svidchenko.hw01.common;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Execution service, that provides multithreding
 */
public final class ThreadingService {
    private final static ExecutorService SERVICE = Executors.newCachedThreadPool();

    public static void run(Runnable runnable) {
        SERVICE.execute(runnable);
    }

    public static <T> Future<T> run(Callable<T> callable) {
        return SERVICE.submit(callable);
    }

    public static void stop() {
        SERVICE.shutdown();
    }
}
