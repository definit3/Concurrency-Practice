package com.definit3.concurrency.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Worker");
            } catch (Exception e) {

            }
            return "Ok";
        }).thenApply(x -> x + x);

        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Worker");
            } catch (Exception e) {

            }
            return "Ok";
        }).orTimeout(0, TimeUnit.SECONDS).exceptionally(s -> "TieoutOccured");
        System.out.println("Main");

        CompletableFuture<Void> f = CompletableFuture.allOf(completableFuture2, completableFuture1);
        f.join();



        try {
            System.out.println(completableFuture1.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println(completableFuture2.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
