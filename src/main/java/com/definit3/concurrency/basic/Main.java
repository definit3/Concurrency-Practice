package com.definit3.concurrency.basic;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main Thread Started");
        MyThread myThread = new MyThread();
        myThread.setPriority(Thread.MIN_PRIORITY);


        MyThreadPriority myThreadPriority = new MyThreadPriority("priorityThread");
        myThreadPriority.setPriority(Thread.MAX_PRIORITY);


        myThreadPriority.start();
        myThread.start();
//        myThreadPriority.interrupt();

        Thread.sleep(1000);

        MyThread daemonThread = new MyThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}