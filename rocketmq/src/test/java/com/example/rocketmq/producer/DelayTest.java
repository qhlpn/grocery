package com.example.rocketmq.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DelayTest {

    @Autowired
    private Delay delay;

    @Test
    void run() throws Exception {
        delay.run();
    }
}