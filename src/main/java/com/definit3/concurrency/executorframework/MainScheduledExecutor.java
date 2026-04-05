package com.definit3.concurrency.executorframework;

import java.util.concurrent.*;

public class MainScheduledExecutor {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        scheduledExecutorService.schedule(
                () -> System.out.println("Task executed after 3 seconds delay"),
                3,
                TimeUnit.SECONDS
        );


        scheduledExecutorService.scheduleAtFixedRate(
                () -> System.out.println("Task executed at fixed rate--overlapping tasks"),
                3,
                5,
                TimeUnit.SECONDS
        );

        scheduledExecutorService.scheduleWithFixedDelay(
                () -> System.out.println("Task executed at fixed delay-- no overlap"),
                3,
                5,
                TimeUnit.SECONDS
        );

        scheduledExecutorService.schedule(
                () -> {
                    System.out.println("Shutdown task");
                    scheduledExecutorService.shutdown();
                },
                20,
                TimeUnit.SECONDS
        );
    }
}
