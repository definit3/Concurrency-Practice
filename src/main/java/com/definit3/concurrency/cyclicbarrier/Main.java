package com.definit3.concurrency.cyclicbarrier;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        int servicesCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(servicesCount, () -> System.out.println("All reached till barrier"));
        Future<String> future1 = executorService.submit(new DependentService(barrier));
        Future<String> future2 = executorService.submit(new DependentService(barrier));
        Future<String> future3 = executorService.submit(new DependentService(barrier));

        barrier.reset();
        executorService.shutdown();
    }
}

class DependentService implements Callable<String> {
    private CyclicBarrier barrier;
    public DependentService(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " service started");
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " is waiting at the barrier");
        barrier.await();

        System.out.println("Continuing further work");
        return "ok";
    }
}
