package com.definit3.concurrency.basic;

public class MyThreadPriority extends Thread {
    public MyThreadPriority(String name) {
        super(name);
    }
    @Override
    public void run() {
        System.out.println("My Priority Thread - " + Thread.currentThread().getName());
        System.out.println("Thread Priority: " + Thread.currentThread().getPriority());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
