package com.definit3.concurrency.concurrentdatastructure;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataStructureMain {

    public static void main(String[] args) throws InterruptedException {
        DataStructure ds = new DataStructure();
        int threadCount = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // Latch so all threads start as close together as possible
        CountDownLatch startGate = new CountDownLatch(1);

        // --- Writers (append) ---
        for (int i = 0; i < 3; i++) {
            final String value = "item-" + i;
            executor.submit(() -> {
                await(startGate);
                ds.write(-1, value);
            });
        }

        // --- Writer (insert at specific index) ---
        executor.submit(() -> {
            await(startGate);
            // Small sleep so there is something in the list to insert into
            sleep(50);
            try {
                ds.write(0, "FIRST");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("[" + Thread.currentThread().getName() + "] Insert at 0 skipped – list still empty");
            }
        });

        // --- Readers ---
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                await(startGate);
                sleep(30); // let writers populate first
                try {
                    String val = ds.read(0);
                    System.out.println("[" + Thread.currentThread().getName() + "] read(0) returned: " + val);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("[" + Thread.currentThread().getName() + "] read(0) – list empty, skipped");
                }
            });
        }

        // --- Searchers ---
        for (int i = 0; i < 2; i++) {
            final String target = "item-" + i;
            executor.submit(() -> {
                await(startGate);
                sleep(40);
                boolean found = ds.search(target);
                System.out.println("[" + Thread.currentThread().getName() + "] search(\"" + target + "\") = " + found);
            });
        }

        // --- Deleter ---
        executor.submit(() -> {
            await(startGate);
            sleep(80); // let writers finish first
            try {
                ds.delete(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("[" + Thread.currentThread().getName() + "] delete(0) – list empty, skipped");
            }
        });

        System.out.println("=== All threads ready – opening start gate ===\n");
        startGate.countDown();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("\n=== Done ===");
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
