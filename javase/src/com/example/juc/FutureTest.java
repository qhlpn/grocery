package com.example.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {


//    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public Future<Integer> calculate(Integer input) {
        return executor.submit(() -> {
            System.out.println("Calculating..." + input);
            Thread.sleep(1000);
            return input * input;
        });
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTest futureTest = new FutureTest();
        Future<Integer> future1 = futureTest.calculate(10);
        Future<Integer> future2 = futureTest.calculate(100);

        while (!(future1.isDone() && future2.isDone())) {
            System.out.println(
                    String.format(
                            "future1 is %s and future2 is %s",
                            future1.isDone() ? "done" : "not done",
                            future2.isDone() ? "done" : "not done"
                    )
            );
            Thread.sleep(300);
        }

        Integer result1 = future1.get();
        Integer result2 = future2.get();

        System.out.println(result1 + " and " + result2);
    }

}
