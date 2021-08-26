package com.example.rocketmq.producer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SQLPropertyTest {

    @Resource(name = "proPro")
    private SQLProperty sqlProperty;

    @Test
    void run() throws Exception {
        sqlProperty.run();
    }
}