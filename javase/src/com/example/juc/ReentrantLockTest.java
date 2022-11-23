package com.example.juc;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    /**
     * 测试可重入性
     */
    @Test
    public void testReentrant() throws InterruptedException {
        SumTask task = new SumTask();
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(SumTask.i);
    }

    static class SumTask implements Runnable {
        public static Lock lock = new ReentrantLock();
        public static int i = 0;

        @Override
        public void run() {
            for (int j = 0; j < 1_000_000; j++) {
                lock.lock();
                lock.lock();
                try {
                    i++;
                } finally {
                    lock.unlock();
                    lock.unlock();
                }
            }
        }
    }


    @Test
    public void testExtraUnlock() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Thread thread = new Thread(() -> {
            lock.lock();
            System.out.println("Locking...");
            lock.unlock();
            lock.unlock();
        });
        thread.start();
        thread.join();
        System.out.println("Finished...");
    }


    @Test
    public void testInterrupt() throws InterruptedException {
        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(2);
        thread.interrupt();
        Thread.sleep(10 * 1000);
        System.out.println("Finished...");
    }

    static class InterruptTask implements Runnable {
        public static volatile int i = 0;
        public ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                lock.lockInterruptibly();
                Thread.sleep(3);
                for (int j = 0; j < 100_000; j++) {
                    i += j;
                }
            } catch (InterruptedException e) {
                System.out.println("InterruptTask was interrupted.");
                System.out.println("i=" + i);
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    System.out.println("i=" + i);
                    lock.unlock();
                }
            }
        }
    }

    @Test
    public void testCondition() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread thread = new Thread(new ConditionTask(lock, condition));
        thread.start();
        Thread.sleep(2000);
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    static class ConditionTask implements Runnable {
        private final Lock lock;
        private final Condition condition;

        public ConditionTask(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                condition.await();
                System.out.println("Thread is going on...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    @Test
    public void testExclusive() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true);
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("t1 : " + LocalDateTime.now());
                    Thread.sleep(10000);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        t1.start();

        Thread.sleep(10);

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("t2 : " + LocalDateTime.now());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        t2.start();

        Thread.sleep(15000);
        System.out.println("DONE");
    }


}
