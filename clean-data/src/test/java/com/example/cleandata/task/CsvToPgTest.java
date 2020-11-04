package com.example.cleandata.task;

import com.example.cleandata.utils.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class CsvToPgTest {

    @Autowired
    CsvToPg csvToPg;

    @Autowired
    SnowFlake snowFlake;

    @Test
    void action01() {
        csvToPg.action01();
    }


    @Test
    void action02() {
        csvToPg.action02();
    }

    @Test
    void test() {
        String s = "124.152.37.0/24,620922,0,0,联通,";
        Pattern p = Pattern.compile("([^,]*),([^,]*),([^,]*),([^,]*),([^,]*),(.*)");
        Matcher m = p.matcher(s);
        System.out.println(m.find());
        System.out.println(m.group(5));
        System.out.println(StringUtils.isBlank(m.group(5)));

    }

}