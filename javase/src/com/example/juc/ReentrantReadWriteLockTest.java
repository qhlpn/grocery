package com.example.juc;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    private volatile int value;

    public int handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getId() + " : read done!");
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock, int value) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(3000);
            this.value = value;
            System.out.println(Thread.currentThread().getId() + " : write done!");
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void test() throws InterruptedException {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        Runnable readTask = () -> {
            try {
                handleRead(readLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable writeTask = () -> {
            try {
                handleWrite(writeLock, new Random().nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 1; i <= 20; i++) {
            if (i % 10 == 3) {
                new Thread(writeTask).start();
            } else {
                new Thread(readTask).start();
            }
        }
        Thread.sleep(20 * 1000);
    }

}
