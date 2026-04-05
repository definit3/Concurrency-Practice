package com.definit3.concurrency.executorframework;

import java.util.concurrent.*;

public class MainExecutorsFuture {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(() -> 42);
        if (future.isDone()) {
            System.out.println("Task is completed");
        } else {
            System.out.println("Task is pending");
        }
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (future.isDone()) {
            System.out.println("Task is completed");
        }

        Callable<Integer> callable = () -> 3;
        future = executorService.submit(callable);
        future.cancel(true);
        try {
            System.out.println(future.get(0, TimeUnit.MILLISECONDS));
        } catch (CancellationException | TimeoutException | InterruptedException | ExecutionException e) {
            System.out.println("Future Timed Out, Cancelled");
        }
        System.out.println(future.isCancelled());
        System.out.println(future.isDone());

        Runnable runnable = () -> System.out.println(3);
        Future<?> futureRunnable = executorService.submit(runnable);
        try {
            futureRunnable.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdownNow();
    }
}
