package com.example.juc;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest {

    @Test
    public void testState() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("StartTime: " + LocalDateTime.now());
            int i = 0;
            try {
                Thread.sleep(10 * 1000);
                while (true) {
                    i++;
                    if (i > Integer.MAX_VALUE >> 1) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("testState: is interrupted at "
                        + LocalDateTime.now());
                e.printStackTrace();
            }
            System.out.println("EndTime: " + LocalDateTime.now());
        });
        // NEW
        System.out.println(thread.getState());
        thread.start();
        // RUNNABLE
        System.out.println(thread.getState());
        Thread.sleep(1000);
        // TIMED_WAITING
        System.out.println(thread.getState());
        Thread.sleep(9200);
        // RUNNABLE ??
        System.out.println(thread.getState());
        Thread.sleep(10 * 1000);
        // TERMINATED
        System.out.println(thread.getState());
    }


    @Test
    public void testBlockState() throws InterruptedException {
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(50);
        System.out.println(t2.getState());
    }


    @Test
    public void testInterrupt() throws InterruptedException {
        class InterruptTask implements Runnable {
            @Override
            public void run() {
                System.out.println(Thread.interrupted());
                Thread thread = Thread.currentThread();
                System.out.println(thread.toString());
                while (true) {
                    if (thread.isInterrupted()) {
                        System.out.println("InterruptTask was interrupted at "
                                + LocalDateTime.now());
                        break;
                    }
                }
            }
        }

        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(5 * 1000);
        thread.interrupt();
        Thread.sleep(5 * 1000);
    }

    @Test
    public void testInterruptStatus1() throws InterruptedException {
        class InterruptTask implements Runnable {
            @Override
            public void run() {
                long i = 0;
                while (true) {
                    i++;
                }
            }
        }
        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        System.out.println(LocalDateTime.now());
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
        System.out.println(LocalDateTime.now());
    }


    @Test
    public void testInterruptStatus2() throws InterruptedException {
        class IntDelay implements Delayed {

            private int num;
            private long deadline;

            public IntDelay(int num) {
                this.num = num;
                deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(num);
            }

            @Override
            public long getDelay(TimeUnit unit) {
                return deadline - System.currentTimeMillis();
            }

            @Override
            public int compareTo(Delayed o) {
                IntDelay param = (IntDelay) o;
                return Integer.compare(this.num, param.num);
            }
        }

        class InterruptTask implements Runnable {
            @Override
            public void run() {
                Thread current = Thread.currentThread();
                DelayQueue<IntDelay> queue = new DelayQueue<>();
                queue.add(new IntDelay(1));
                try {
                    System.out.println("Wait  " + LocalDateTime.now());
                    queue.take();
                    System.out.println("Taken " + LocalDateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("current.isInterrupted() = " + current.isInterrupted());
                System.out.println("current.isInterrupted() = " + current.isInterrupted());
            }
        }

        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
    }


    @Test
    public void testWaitLock() throws InterruptedException {
        // 测试 wait 是否释放锁
        // 根据运行结果来看，thread1 和 thread2 是交叉执行的，
        // 则：线程在 wait 时，是释放了锁的，
        // 再次获取锁后，会接着上次执行点继续执行。
        //
        // 这里还有一点需要注意：wait 需要在锁对象上执行，否则会报错。
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    lock.wait(1000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(2000);
    }

    @Test
    public void testSleepLock() throws InterruptedException {
        // 测试 sleep 是否释放锁
        // 根据输出来看，thread1 执行完后再次执行的 thread2
        // 则：线程在 sleep 时，不释放锁。
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    Thread.sleep(2000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(3000);
    }

    @Test
    public void testJoin() throws InterruptedException {
        JoinMain.AddThread thread = new JoinMain.AddThread();
        thread.start();
        // 执行这句话，则下面的输出会等 thread 执行完成后，i值等于100000；
        // 如果注释掉，则瞬间向下执行，i值很小。
        // join: Waits for this thread to die.
        thread.join();
        System.out.println(JoinMain.i);
    }

    static class JoinMain {
        public volatile static int i = 0;

        static class AddThread extends Thread {
            @Override
            public void run() {
                for (i = 0; i < 100000; i++) {
                }
            }
        }
    }


    @Test
    public void testYield() throws InterruptedException {
        Map<Integer, Integer> map = new HashMap<>();
        Integer key = 1;
        Integer key2 = 2;
        Thread thread = new Thread(() -> {
            while (true) {
                // yield: the current thread is willing to yield its current use of a processor.
                Thread.yield();
                Integer num = map.getOrDefault(key, 1);
                map.put(key, ++num);
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                Integer num = map.getOrDefault(key2, 1);
                map.put(key2, ++num);
            }
        });
        thread.start();
        thread2.start();
        Thread.sleep(1000);
        // 如果 Thread.yield() 没有让出 CPU，则两个值相差不多；否则相差很大。
        System.out.println(map.toString());
        System.out.println(thread.getState());
    }

    @Test
    public void testChildThread() {
        List<Thread> threads = new ArrayList<>();
        Thread thread1 = new Thread(() -> {
            Thread thread = Thread.currentThread();
//            ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 119);
//            System.out.println(threadLocal.get());

            ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
            threadLocal.set(1);
            Thread child = new Thread(() -> {
                threadLocal.set(2);
                System.out.println("c: " + threadLocal.get());
            });
            child.start();
            System.out.println("p: " + threadLocal.get());
        });
        threads.add(thread1);
        thread1.start();
    }


    @Test
    public void testInterruptNoAction() {
        // 虽然给线程发出了中断信号，但程序中并没有响应中断信号的逻辑，所以程序不会有任何反应。
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("b: " + Thread.currentThread().isInterrupted());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("d: " + Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.park();
    }


    @Test
    public void testInterruptAction() {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
    }

    @Test
    public void testInterruptFailure() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }

                try {
                    // sleep() 方法被中断后会清除中断标记，所以循环会继续运行。。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java技术栈线程休眠被中断，程序退出。");
                }
                System.out.println(Thread.currentThread().getState() + " 线程苏醒，继续执行……");
            }
        });
        thread.start();
        Thread.sleep(100); // 注意加上这句话！否则线程还没启动就被终端了
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testInterruptSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java技术栈线程被中断，程序退出。");
                    return;
                }

                try {
                    // sleep() 方法被中断后会清除中断标记，所以循环会继续运行。。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java技术栈 线程 休眠被中断，程序退出。");
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getState() + " 线程苏醒，继续执行……");
            }
        });
        thread.start();
        Thread.sleep(100); // 注意加上这句话！否则线程还没启动就被终端了
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testSynchronized() throws InterruptedException {
        class Account {
            int money = 100;

            synchronized void increase() {
                System.out.println("start to increase");
                money -= 10;
                double var = 0;
                for (int i = 0; i < 10000000; i++) {
                    var = Math.PI * Math.E * i;
                    if (i % 2000000 == 0) {
                        throw new RuntimeException("fire");
                    }
                }
                System.out.println("finish increasing." + var);
            }

            synchronized void decrease() {
                System.out.println("start to decrease");
                money += 20;
                System.out.println("finish decreasing.");
            }
        }
        Account account = new Account();
        new Thread(account::increase).start();
        Thread.sleep(1);
        new Thread(account::decrease).start();
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(30));
        System.out.println(account.money);
    }

}
