package com.example.rocketmq.producer;

import com.example.rocketmq.producer.base.Sync;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SyncTest {

    @Autowired
    private Sync sync;

    @Test
    public void run() throws Exception {
        sync.run();
    }


}