package com.definit3.concurrency.countdownlatch;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<String> future1 = executorService.submit(new DependentService());
        Future<String> future2 = executorService.submit(new DependentService());
        Future<String> future3 = executorService.submit(new DependentService());

        future1.get();
        future2.get();
        future3.get();

        System.out.println("All dependent service finished");


        int servicesCount = 3;
        executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(servicesCount);
        executorService.submit(new DependentServiceWithLatch(latch));
        executorService.submit(new DependentServiceWithLatch(latch));
        executorService.submit(new DependentServiceWithLatch(latch));
        latch.await(0, TimeUnit.SECONDS);

        System.out.println("Latch await finished");
        executorService.shutdown();
    }
}

class DependentService implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " service started");
        Thread.sleep(2000);
        return "ok";
    }
}

class DependentServiceWithLatch implements Callable<String> {
    private CountDownLatch latch;
    public DependentServiceWithLatch(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public String call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + " service started");
            Thread.sleep(2000);
            return "ok";
        } finally {
            latch.countDown();
        }
    }
}
