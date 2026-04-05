package com.definit3.concurrency.explicit.unfairlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairLockExample {
    private final Lock unfairLock = new ReentrantLock(true);

    public void accessResource() {
        unfairLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired lock");
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        } finally {
            System.out.println(Thread.currentThread().getName() + " released lock");
            unfairLock.unlock();
        }
    }

    public static void main(String[] args) {
        FairLockExample unfairLockExample = new FairLockExample();
        Runnable task = () -> {
            unfairLockExample.accessResource();
        };
        Thread thread1 = new Thread(task, "Thread 1");
        Thread thread2 = new Thread(task, "Thread 2");
        Thread thread3 = new Thread(task, "Thread 3");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
