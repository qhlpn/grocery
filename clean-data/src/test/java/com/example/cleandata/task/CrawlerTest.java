package com.example.cleandata.task;

import com.example.cleandata.task.CsvToPgTest.Inner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CrawlerTest {

    @Autowired
    Crawler crawler;

    @Test
    void action() {

        int b = new Inner().b;
        int a = Inner.a;

    }


}