package com.example.rocketmq.consumer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SQLPropertyTest {

    @Resource(name = "proCon")
    private SQLProperty sqlProperty;

    @Test
    void run() throws MQClientException, InterruptedException {
        sqlProperty.run();

    }
}