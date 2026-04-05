package com.definit3.concurrency.explicit.lock;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        BankAccount bankAccount = new BankAccount();
        Runnable task = () -> {
                try {
                    bankAccount.withdraw(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        };
        Thread t1 = new Thread(task, "Thread 1");
        Thread t2 = new Thread(task, "Thread 2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Current balance: " + bankAccount.getBalance());
    }
}
