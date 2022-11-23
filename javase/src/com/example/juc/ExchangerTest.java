package com.example.juc;

import java.util.concurrent.Exchanger;


/**
 * 两个线程之间交换持有的对象。
 * 当Exchanger在一个线程中调用exchange方法之后，会等待另外的线程调用同样的exchange方法
 */
public class ExchangerTest {

    private static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                String aa = exchanger.exchange("V1");
                System.out.println(Thread.currentThread().getName() + aa);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T1").start();

        new Thread(()->{
            try {
                String bb = exchanger.exchange("V2");
                System.out.println(Thread.currentThread().getName() + bb);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2").start();
    }

}
