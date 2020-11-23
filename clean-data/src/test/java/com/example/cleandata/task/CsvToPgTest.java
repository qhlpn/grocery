package com.example.cleandata.task;

import com.example.cleandata.utils.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CsvToPgTest {

    public static class Inner {

        public static int a;
        public int b;
    }

    @Autowired
    CsvToPg csvToPg;

    @Autowired
    SnowFlake snowFlake;

    @Test
    void action01() {
//        csvToPg.action01();
    }


    @Test
    void action02() {
//        csvToPg.action02();
    }


}