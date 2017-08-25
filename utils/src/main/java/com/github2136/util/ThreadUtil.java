package com.github2136.util;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工具类
 * Created by yubin on 2017/8/23.
 */

public class ThreadUtil {
    private static volatile ThreadUtil instance;

    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 128;
    private static final long keepAliveTime = 1;
    private static final TimeUnit unit = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static final RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

    public static ThreadUtil getInstance() {
        return getInstance("thread");
    }

    public static ThreadUtil getInstance(String name) {
        return getInstance(name, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ThreadUtil getInstance(String name, int corePoolSize,
                                         int maximumPoolSize,
                                         long keepAliveTime,
                                         TimeUnit unit,
                                         BlockingQueue<Runnable> workQueue,
                                         ThreadFactory threadFactory,
                                         RejectedExecutionHandler handler) {
        if (instance == null) {
            synchronized (ThreadUtil.class) {
                if (instance == null) {
                    instance = new ThreadUtil();
                }
            }
        }
        instance.getExecutor(name,
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
        return instance;
    }

    public static ThreadUtil getInstance(int corePoolSize,
                                         int maximumPoolSize,
                                         long keepAliveTime,
                                         TimeUnit unit,
                                         BlockingQueue<Runnable> workQueue,
                                         ThreadFactory threadFactory,
                                         RejectedExecutionHandler handler) {
        return getInstance("thread", corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private Map<String, ThreadPoolExecutor> mapExecutor;
    private ThreadPoolExecutor executor = null;

    private ThreadUtil() {
        mapExecutor = new HashMap<>();
    }

    private void getExecutor(String name,
                             int corePoolSize,
                             int maximumPoolSize,
                             long keepAliveTime,
                             TimeUnit unit,
                             BlockingQueue<Runnable> workQueue,
                             ThreadFactory threadFactory,
                             RejectedExecutionHandler handler) {
        executor = mapExecutor.get(name);
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    unit,
                    workQueue,
                    threadFactory,
                    handler);
            mapExecutor.put(name, executor);
        }
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
