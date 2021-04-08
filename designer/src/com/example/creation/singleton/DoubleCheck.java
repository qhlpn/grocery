package com.example.creation.singleton;

import java.util.concurrent.*;

public class DoubleCheck {

    private static volatile DoubleCheck instance = null;

    private DoubleCheck() {

    }

    public static DoubleCheck getInstance() {
        if (instance == null) {
            synchronized (DoubleCheck.class) {
                if (instance == null) {
                    instance = new DoubleCheck();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(10,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                try {
                    System.out.println(DoubleCheck.getInstance().toString());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();

    }
}
