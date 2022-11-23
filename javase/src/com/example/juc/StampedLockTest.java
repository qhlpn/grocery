package com.example.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.StampedLock;

/**
 * ReentrantReadWriteLock 的性能已经很好了但是他底层还是需要进行一系列的cas操作去加锁；
 * StampedLock如果是读锁上锁是没有这种cas操作的性能比ReentrantReadWriteLock 更好
 * 也称为乐观读锁；即读获取锁的时候 是不加锁 直接返回一个值；然后执行临界区的时候去验证这个值是否有被人修改（写操作加锁）
 * 如果没有被人修改则直接执行临界区的代码；如果被人修改了则需要升级为读写锁
 *
 *
 */
public class StampedLockTest {
    @Test
    public void test() throws InterruptedException {
        StampedLock lock = new StampedLock();
        Point point = new Point(lock);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                point.move(random.nextDouble(100), random.nextDouble(100));
                System.out.println("move point...");
            });

            executorService.execute(() -> {
                double distance = point.distanceFromOrigin();
                System.out.println("current distance = " + distance);
            });
        }

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                double distance = point.distanceFromOrigin();
                System.out.println("current distance = " + distance);
            });
        }

        Thread.sleep(100000);
    }

    static class Point {
        private volatile double x, y;
        private final StampedLock lock;

        public Point(StampedLock lock) {
            this.lock = lock;
        }

        /**
         * 通过 writeLock 以获取写锁
         */
        void move(double deltaX, double deltaxY) {
            long stamp = lock.writeLock();
            try {
                x += deltaX;
                y += deltaxY;
            } finally {
                lock.unlock(stamp);
            }
        }

        /**
         * 先使用 tryOptimisticRead 获取锁的邮戳，通过 validate 校验锁版本是否正确，
         * 此时并没有进行锁获取，这是一种乐观的模式。如果校验发现锁版本已被修改，则可以通过 readLock 以获取共享锁，然后重新进行数据读取。
         */
        double distanceFromOrigin() {
            long stamp = lock.tryOptimisticRead();
            double currentX = x, currentY = y;
            if (!lock.validate(stamp)) {
                System.out.println("is changed ...");
                stamp = lock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    lock.unlock(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }
}