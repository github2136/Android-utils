package com.github2136.util;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工具类
 * Created by yb on 2017/8/23.
 */
public class ThreadUtil {
    private static volatile ThreadUtil instance;
    private static final int CPUCount = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = CPUCount + 1;
    private static final int maximumPoolSize = CPUCount * 2 + 1;
    private static final long keepAliveTime = 1;
    private static final TimeUnit unit = TimeUnit.SECONDS;
    private static final int queueSize = 128;
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueSize);
    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static final RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();


    public static ThreadUtil getInstance() {
        return getInstance(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ThreadUtil getInstance(int corePoolSize,
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
        instance.getExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
        return instance;
    }

    public static ThreadUtil getNewInstance() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueSize);
        ThreadFactory threadFactory = new ThreadFactory() {
            AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
            }
        };
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        return getNewInstance(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ThreadUtil getNewInstance(int corePoolSize,
                                            int maximumPoolSize,
                                            long keepAliveTime,
                                            TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue,
                                            ThreadFactory threadFactory,
                                            RejectedExecutionHandler handler) {
        ThreadUtil instance = new ThreadUtil();
        instance.getExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);
        return instance;
    }

    private ThreadPoolExecutor executor = null;

    private ThreadUtil() { }

    private void getExecutor(int corePoolSize,
                             int maximumPoolSize,
                             long keepAliveTime,
                             TimeUnit unit,
                             BlockingQueue<Runnable> workQueue,
                             ThreadFactory threadFactory,
                             RejectedExecutionHandler handler) {
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    unit,
                    workQueue,
                    threadFactory,
                    handler);
        }
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

//    public Executor getNew() {
//        return aNew;
//    }
}
