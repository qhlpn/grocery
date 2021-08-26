package com.example.rocketmq.producer;

import com.example.rocketmq.producer.base.Async;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsyncTest {

    @Autowired
    private Async async;

    @Test
    void run() throws Exception {
        async.run();
    }
}