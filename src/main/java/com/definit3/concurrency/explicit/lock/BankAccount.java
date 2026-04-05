package com.definit3.concurrency.explicit.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private int balance = 50;

    private final Lock lock = new ReentrantLock();

    public void withdraw(int amount) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " attempting to withdraw " + amount + " curr Balance: " + balance);
        if (lock.tryLock(15000, TimeUnit.MILLISECONDS) ) {
            if (balance >= amount) {
                System.out.println(Thread.currentThread().getName() + " proceeding with withdrawal");
                Thread.sleep(10000);
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " Completed withdrawal");
            } else {
                System.out.println("Insufficient Balance");
            }
            lock.unlock();
        } else {
            System.out.println("Couldn't acquire lock, timed out");
        }
    }

    public int getBalance() {
        return balance;
    }
}
