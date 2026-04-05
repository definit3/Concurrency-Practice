package com.definit3.concurrency.basic;

public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread My Thread Started...");
        try {
            Thread.yield();

            System.out.println("Thread - My Thread Sleep Starting...");
            Thread.sleep(1000);
            System.out.println("Thread - My Thread Sleep Ended...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
