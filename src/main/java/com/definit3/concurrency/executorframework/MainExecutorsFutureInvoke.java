package com.definit3.concurrency.executorframework;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MainExecutorsFutureInvoke {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<Integer> callable1 = () -> {
            System.out.println("Executing callable 1");
            Thread.sleep(1000);
            return 1;
        };
        Callable<Integer> callable2 = () -> {
            System.out.println("Executing callable 2");
            Thread.sleep(1000);
            return 2;
        };
        Callable<Integer> callable3 = () -> 3;

        List<Callable<Integer>> list = Arrays.asList(callable1, callable2, callable3);
        List<Future<Integer>> futures = null;
        try {
             futures = executorService.invokeAll(list, 1, TimeUnit.SECONDS); //invoke all will block till all results are calculated
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e);
        }
        for (Future future: futures) {
            try {
                System.out.println(future.get());
            } catch (CancellationException | InterruptedException | ExecutionException e) {
                System.out.println("Interrupted: " + e);
            }
        }


        try {
            Integer integer = executorService.invokeAny(list);
            System.out.println(integer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdownNow();
    }
}
