package com.example.rocketmq.producer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SequenceTest {

    @Resource(name = "seqPro")
    private Sequence sequence;

    @Test
    void run() throws Exception {
        sequence.run();
    }
}