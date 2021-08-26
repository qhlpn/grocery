package com.example.rocketmq.consumer;

import com.example.rocketmq.consumer.base.BroadCast;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BroadCastTest {

    @Autowired
    private BroadCast broadCast;

    @Test
    void run() throws MQClientException, InterruptedException {
        broadCast.run();
    }
}