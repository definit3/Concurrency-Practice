package com.definit3.concurrency.executorframework;

import java.util.concurrent.*;

public class MainExecutorsFutureResult {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future =  executorService.submit(() -> System.out.println("Hello"), "Succuessful");
        System.out.println(future.get());
        executorService.shutdownNow();
    }
}
