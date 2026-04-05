package com.definit3.concurrency.explicit.readwritelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteCounter {
    private int count = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void increment() {
        writeLock.lock();
        try {
            count++;
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public int getCount() {
        readLock.lock();
        try {
            return count;
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteCounter counter = new ReadWriteCounter();
        Runnable readTask = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " read: " + counter.getCount());
            }
        };
        Runnable writeTask = () -> {
           for (int i = 0; i < 10; i++) {
               counter.increment();
               System.out.println(Thread.currentThread().getName() + " incremented");
           }
        };

        Thread thread1 = new Thread(readTask, "readThread1");
        Thread thread2 = new Thread(readTask, "readThread2");
        Thread thread3 = new Thread(writeTask, "writeThread");
        thread3.start();
        thread2.start();
        thread1.start();
        thread3.join();
        thread2.join();
        thread1.join();
        System.out.println("Counter value: " + counter.getCount());
    }
}
