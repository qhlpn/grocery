package com.example.cleandata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
class CleanDataApplicationTests {


    @Test
    void test() {
//        String s = "124.152.37.0/24,620922,0,0,联通,";
//        Pattern p = Pattern.compile("([^,]*),([^,]*),([^,]*),([^,]*),([^,]*),(.*)");
//        Matcher m = p.matcher(s);
//        System.out.println(m.find());
//        System.out.println(m.group(5));
//        System.out.println(StringUtils.isBlank(m.group(5)));
    }

}
