package com.example.structure.flyweight;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Caller {

    private static ServiceFactory serviceFactory = new ServiceFactory();

    public static void main(String[] args) {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(10,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 100; i++) {
            final int t = i % 10;
            pool.execute(() -> {
                try {
                    System.out.println(t + ":" + serviceFactory.createService(t).toString());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();


    }


}
