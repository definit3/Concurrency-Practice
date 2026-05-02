package com.definit3.concurrency.taskscheduler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultTaskScheduler implements TaskScheduler {
    Lock lock = new ReentrantLock(true);
    Condition condition = lock.newCondition();
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    PriorityQueue<ScheduledTask> pq = new PriorityQueue<>(
            (a, b) -> {
                if (a.getExecutionTime().equals(b.getExecutionTime())) {
                    return 0;
                } else if (a.getExecutionTime() < b.getExecutionTime()) {
                    return -1;
                } else return 1;
            });

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static String t() {
        return "[" + LocalTime.now().format(FMT) + "][" + Thread.currentThread().getName() + "]";
    }

    @Override
    public void submitOneTimeTask(Long initialDelay, Runnable runnable) {
        ScheduledTask scheduledTask = new ScheduledTask(runnable, System.currentTimeMillis() + initialDelay, UUID.randomUUID().toString(), TaskType.ONE_TIME, 0L);
        try {
            lock.lock();
            System.out.println(t() + " ENQUEUE   ONE_TIME   taskId=" + scheduledTask.getTaskId() + " runAt=now+" + initialDelay + "ms  queueSize=" + (pq.size() + 1));
            pq.add(scheduledTask);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void submitTaskWithFixedRate(Long initialDelay, Long scheduledDelay, Runnable runnable) {
        ScheduledTask scheduledTask = new ScheduledTask(runnable, System.currentTimeMillis() + initialDelay, UUID.randomUUID().toString(), TaskType.FIXED_RATE, scheduledDelay);
        try {
            lock.lock();
            System.out.println(t() + " ENQUEUE   FIXED_RATE  taskId=" + scheduledTask.getTaskId() + " runAt=now+" + initialDelay + "ms  period=" + scheduledDelay + "ms  queueSize=" + (pq.size() + 1));
            pq.add(scheduledTask);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void submitTaskWithFixedDelay(Long initialDelay, Long scheduledDelay, Runnable runnable) {
        ScheduledTask scheduledTask = new ScheduledTask(runnable, System.currentTimeMillis() + initialDelay, UUID.randomUUID().toString(), TaskType.FIXED_DELAY, scheduledDelay);
        try {
            lock.lock();
            System.out.println(t() + " ENQUEUE   FIXED_DELAY taskId=" + scheduledTask.getTaskId() + " runAt=now+" + initialDelay + "ms  delay=" + scheduledDelay + "ms  queueSize=" + (pq.size() + 1));
            pq.add(scheduledTask);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        System.out.println(t() + " Scheduler thread started");
        while (true) {
            try {
                lock.lock();
                while (pq.isEmpty()) {
                    System.out.println(t() + " Queue empty — waiting for tasks");
                    condition.await();
                }
                ScheduledTask head = pq.peek();
                long delay = head.getExecutionTime() - System.currentTimeMillis();
                if (delay > 0) {
                    System.out.println(t() + " Next task taskId=" + head.getTaskId() + " type=" + head.getTaskType() + " not ready — waiting " + delay + "ms");
                    condition.await(delay, TimeUnit.MILLISECONDS);
                    continue;
                }
                ScheduledTask task = pq.poll();
                System.out.println(t() + " DISPATCH  " + task.getTaskType() + "  taskId=" + task.getTaskId() + "  queueSize=" + pq.size());

                if (task.getTaskType().equals(TaskType.ONE_TIME)) {
                    executorService.submit(() -> {
                        System.out.println(t() + " EXECUTING ONE_TIME   taskId=" + task.getTaskId());
                        task.getRunnable().run();
                        System.out.println(t() + " DONE      ONE_TIME   taskId=" + task.getTaskId());
                    });

                } else if (task.getTaskType().equals(TaskType.FIXED_RATE)) {
                    long nextTime = task.getExecutionTime() + task.getScheduledDelay();
                    task.setExecutionTime(nextTime);
                    pq.add(task);
                    System.out.println(t() + " RE-ENQUEUE FIXED_RATE  taskId=" + task.getTaskId() + " nextRunIn=" + (nextTime - System.currentTimeMillis()) + "ms  queueSize=" + pq.size());
                    executorService.submit(() -> {
                        System.out.println(t() + " EXECUTING FIXED_RATE  taskId=" + task.getTaskId());
                        task.getRunnable().run();
                        System.out.println(t() + " DONE      FIXED_RATE  taskId=" + task.getTaskId());
                    });

                } else if (task.getTaskType().equals(TaskType.FIXED_DELAY)) {
                    executorService.submit(() -> {
                        System.out.println(t() + " EXECUTING FIXED_DELAY taskId=" + task.getTaskId());
                        task.getRunnable().run();
                        System.out.println(t() + " DONE      FIXED_DELAY taskId=" + task.getTaskId() + " — re-enqueueing after " + task.getScheduledDelay() + "ms delay");
                        try {
                            lock.lock();
                            task.setExecutionTime(System.currentTimeMillis() + task.getScheduledDelay());
                            pq.add(task);
                            System.out.println(t() + " RE-ENQUEUE FIXED_DELAY taskId=" + task.getTaskId() + " queueSize=" + pq.size());
                            condition.signal();
                        } finally {
                            lock.unlock();
                        }
                    });
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
