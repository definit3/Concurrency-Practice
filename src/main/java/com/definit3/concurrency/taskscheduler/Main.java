package com.definit3.concurrency.taskscheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DefaultTaskScheduler taskScheduler = new DefaultTaskScheduler();

        // dedicated thread to run the scheduler loop
        ExecutorService schedulerRunner = Executors.newSingleThreadExecutor(r -> new Thread(r, "scheduler-thread"));
        schedulerRunner.submit(taskScheduler);

        // ONE_TIME: fires once after 2s
        taskScheduler.submitOneTimeTask(2000L,
                () -> System.out.println("  >> ONE_TIME task ran on " + Thread.currentThread().getName()));

        // FIXED_RATE: first run after 1s, then every 3s
        // next execution = last scheduled time + period (clock-based, doesn't care how long task took)
        taskScheduler.submitTaskWithFixedRate(1000L, 3000L,
                () -> {
                    System.out.println("  >> FIXED_RATE task START on " + Thread.currentThread().getName());
                    sleep(1500); // simulates a task that takes 1.5s — still fires every 3s from schedule
                    System.out.println("  >> FIXED_RATE task END");
                });

        // FIXED_DELAY: first run after 1s, then 3s AFTER each completion
        // next execution = finish time + delay (waits for task to finish first)
        taskScheduler.submitTaskWithFixedDelay(1000L, 3000L,
                () -> {
                    System.out.println("  >> FIXED_DELAY task START on " + Thread.currentThread().getName());
                    sleep(1500); // simulates a task that takes 1.5s — next run is 3s after this finishes
                    System.out.println("  >> FIXED_DELAY task END");
                });

        // keep main alive long enough to observe multiple recurring executions
        System.out.println("=== Scheduler running — observing for 20s ===");
        TimeUnit.SECONDS.sleep(20);

        schedulerRunner.shutdownNow();
        System.out.println("=== Done ===");
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
