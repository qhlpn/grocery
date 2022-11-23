package com.example.juc;

import java.util.concurrent.*;
import java.util.function.Supplier;


/**
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待。
 * 从Java 8开始引入了CompletableFuture，它针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法。
 */
public class CompletableFutureTest {

    public static void main(String[] args) throws Exception {

//        // JDK8中有双冒号的用法，就是把方法当做参数传到stream内部，使stream的每个元素都传入到该方法里面执行一下
//        // fetchPrice()静态方法的签名符合Supplier接口的定义（除了方法名外）
//        Supplier<Double> fetchPrice = CompletableFutureTest::fetchPrice;
//        // 创建异步执行任务:
//        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(fetchPrice);
//        // 如果执行成功:
//        cf.thenAccept((result) -> {
//            System.out.println("price: " + result);
//        });
//        // 如果执行异常:
//        cf.exceptionally((e) -> {
//            e.printStackTrace();
//            return null;
//        });
//        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
//        Thread.sleep(10000);



        // 同时从新浪和网易查询证券代码，只要任意一个返回结果，就进行下一步查询价格，查询价格也同时从新浪和网易查询，只要任意一个返回结果，就完成操作
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://finance.sina.com.cn/code/"));
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://money.163.com/code/"));

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> fetchPrice((String) code, "https://finance.sina.com.cn/price/"));
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> fetchPrice((String) code, "https://money.163.com/price/"));

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(20000);

        // 除了anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”，这些组合操作可以实现非常复杂的异步流程控制。




    }

    static Double fetchPrice() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }

    static String queryCode(String name, String url) {
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
        }
        System.out.println("query code from " + url + "...");
        return "601857";
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }

}
