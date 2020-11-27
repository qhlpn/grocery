package com.example.kafkazookeper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaZookeeperApplicationTests {

    @Autowired
    private com.example.kafkazookeper.Service.Test test;


    @Test
    void contextLoads() throws InterruptedException {
        test.test();
        while (true) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Thread.sleep(10000);
        }
    }

}
