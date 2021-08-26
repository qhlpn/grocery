package com.example.rocketmq.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionTest {

    @Autowired
    private Transaction transaction;

    @Test
    void run() throws Exception {
        transaction.run();
    }
}