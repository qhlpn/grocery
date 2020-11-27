package com.example.kafkazookeper.Service;

import com.example.kafkazookeper.Pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author QiuHongLong
 */
@Component
public class Test {

    private final MsgProducer msgProducer;

    private final MsgConsumer msgConsumer;

    private AtomicLong atomicLong = new AtomicLong();

    public Test(MsgProducer msgProducer, MsgConsumer msgConsumer) {
        this.msgProducer = msgProducer;
        this.msgConsumer = msgConsumer;
    }

    public void test() {

        final int CORE_POOL_SIZE = 5;
        final int MAX_POOL_SIZE = 10;
        final int QUEUE_CAPACITY = 100;
        final Long KEEP_ALIVE_TIME = 1L;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());

        executor.execute(() -> {

            while (true) {

                Message m1 = Message.builder().id(atomicLong.addAndGet(1)).msg("qhl").build();
                msgProducer.sendMessage("topic1", m1);

                Message m2 = Message.builder().id(atomicLong.addAndGet(1)).msg("ljj").build();
                msgProducer.sendMessage("topic1", m2);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        executor.shutdown();
    }

}
