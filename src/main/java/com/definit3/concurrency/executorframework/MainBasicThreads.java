package com.definit3.concurrency.executorframework;

public class MainBasicThreads {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[9];
        for (int i = 1; i < 10; i++) {
            int finalI = i;
            threads[i-1]  = new Thread(() -> {
                long result = factorial(finalI);
                System.out.println(result);
            });
            threads[i-1].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("time taken: " + (System.currentTimeMillis() - startTime));
    }
    private static long factorial(int n) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
