package com.example.cleandata.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CsvToPgTest {

    @Autowired
    CsvToPg csvToPg;

    @Test
    void action01() {
        csvToPg.action01();

    }

}