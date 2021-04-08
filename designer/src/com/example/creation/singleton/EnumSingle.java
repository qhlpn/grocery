package com.example.creation.singleton;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum EnumSingle {

    INSTANCE("a", "b", "c");

    private String field1;
    private String field2;
    private String field3;

    EnumSingle(String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }



    public static void main(String[] args) {

        // 枚举单例： 线程安全 + 防止序列化生成 + 防止反射攻击

        ThreadPoolExecutor pool = new ThreadPoolExecutor(10,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                try {
                    System.out.println(EnumSingle.INSTANCE.hashCode());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();

    }



}
