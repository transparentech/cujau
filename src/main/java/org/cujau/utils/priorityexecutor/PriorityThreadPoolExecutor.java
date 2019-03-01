package org.cujau.utils.priorityexecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Prioritized ThreadPoolExecutor.
 *
 * Taken from StackOverflow: https://stackoverflow.com/questions/3545623/how-to-implement-priorityblockingqueue-with-threadpoolexecutor-and-custom-tasks/5485769#5485769
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      int initialQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
              new PriorityBlockingQueue<>(initialQueueSize, new PriorityFutureComparator()));
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      ThreadFactory threadFactory, int initialQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
              new PriorityBlockingQueue<>(initialQueueSize, new PriorityFutureComparator()), threadFactory);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      RejectedExecutionHandler handler, int initialQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
              new PriorityBlockingQueue<>(initialQueueSize, new PriorityFutureComparator()), handler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      ThreadFactory threadFactory, RejectedExecutionHandler handler,
                                      int initialQueueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
              new PriorityBlockingQueue<>(initialQueueSize, new PriorityFutureComparator()), threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        RunnableFuture<T> newTaskFor = super.newTaskFor(callable);
        return new PriorityFuture<>(newTaskFor, ((PriorityCallable<T>) callable).getPriority());
    }
}
