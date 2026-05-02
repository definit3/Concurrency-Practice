package com.definit3.concurrency.filereadwrite;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        File file = new File("1", "readwrite", "hello world");

        Runnable readRunnable1 = () -> {
            System.out.println(file.read(2, 5));
        };

        Runnable readRunnable2 = () -> {
            System.out.println(file.read(3, 6));
        };

        Runnable writeRunnable = () -> System.out.println(file.readWrite(2, "new hello"));

        Runnable writeRunnable2 = () -> {
            System.out.println(file.readWrite(1, "new hello"));
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(readRunnable1);
        executorService.submit(readRunnable2);
        executorService.submit(writeRunnable2);
        executorService.submit(writeRunnable);
    }
}
