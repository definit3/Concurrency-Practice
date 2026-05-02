package com.definit3.concurrency.filereadwrite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class File {
    String fileId;
    String fileName;
    StringBuilder fileContent;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    public File(String fileId, String fileName, String fileContent) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileContent = new StringBuilder(fileContent);
    }

    public String read(int offset, int noOfCharacter) {
        System.out.println(Thread.currentThread().getName() + " | acquiring read lock");
        readLock.lock();
        System.out.println(Thread.currentThread().getName() + " | read lock acquired");
        try {
            String result = fileContent.substring(offset, offset + noOfCharacter);
            System.out.println(Thread.currentThread().getName() + " | reading offset=" + offset + " length=" + noOfCharacter + " => \"" + result + "\"");
            return result;
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " | read lock released");
        }
    }

    public String readWrite(int offset, String insertCharacters) {
        System.out.println(Thread.currentThread().getName() + " | acquiring write lock");
        writeLock.lock();
        System.out.println(Thread.currentThread().getName() + " | write lock acquired");
        try {
            System.out.println(Thread.currentThread().getName() + " | inserting \"" + insertCharacters + "\" at offset=" + offset + " | before: \"" + fileContent + "\"");
            fileContent.insert(offset, insertCharacters);
            System.out.println(Thread.currentThread().getName() + " | after: \"" + fileContent + "\"");
            return fileContent.toString();
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + " | write lock released");
        }
    }

    public String getFileContent() {
        System.out.println(Thread.currentThread().getName() + " | acquiring read lock");
        readLock.lock();
        System.out.println(Thread.currentThread().getName() + " | read lock acquired");
        try {
            String result = fileContent.toString();
            System.out.println(Thread.currentThread().getName() + " | getFileContent => \"" + result + "\"");
            return result;
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " | read lock released");
        }
    }
}
