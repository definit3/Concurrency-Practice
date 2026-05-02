package com.definit3.concurrency.concurrentdatastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStructure {
    private final List<String> list = new ArrayList<>();
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();
    Lock mutex = new ReentrantLock(true);

    private static String thread() {
        return "[" + Thread.currentThread().getName() + "]";
    }

    public String read(int index) {
        System.out.println(thread() + " Acquiring READ lock → read(index=" + index + ")");
        try {
            readLock.lock();
            String val = list.get(index);
            System.out.println(thread() + " READ lock held   → read(index=" + index + ") = \"" + val + "\"");
            return val;
        } finally {
            System.out.println(thread() + " Releasing READ lock ← read(index=" + index + ")");
            readLock.unlock();
        }
    }

    public void write(int index, String s) {
        System.out.println(thread() + " Acquiring MUTEX + READ lock → write(index=" + index + ", value=\"" + s + "\")");
        try {
            mutex.lock();
            readLock.lock();
            if (index == -1) {
                System.out.println(thread() + " MUTEX+READ held  → APPEND \"" + s + "\" (list size before: " + list.size() + ")");
                list.add(s);
            } else {
                System.out.println(thread() + " MUTEX+READ held  → INSERT \"" + s + "\" at index " + index + " (list size before: " + list.size() + ")");
                list.add(index, s);
            }
        } finally {
            System.out.println(thread() + " Releasing MUTEX + READ lock ← write(value=\"" + s + "\")");
            mutex.unlock();
            readLock.unlock();
        }
    }

    public boolean search(String s) {
        System.out.println(thread() + " Acquiring READ lock → search(\"" + s + "\")");
        try {
            readLock.lock();
            boolean found = list.contains(s);
            System.out.println(thread() + " READ lock held   → search(\"" + s + "\") = " + found);
            return found;
        } finally {
            System.out.println(thread() + " Releasing READ lock ← search(\"" + s + "\")");
            readLock.unlock();
        }
    }

    public void delete(int index) {
        System.out.println(thread() + " Acquiring WRITE lock → delete(index=" + index + ")");
        try {
            writeLock.lock();
            String removed = list.remove(index);
            System.out.println(thread() + " WRITE lock held  → DELETED \"" + removed + "\" at index " + index + " (list size after: " + list.size() + ")");
        } finally {
            System.out.println(thread() + " Releasing WRITE lock ← delete(index=" + index + ")");
            writeLock.unlock();
        }
    }
}
