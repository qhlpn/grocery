package com.example.cleandata;

import com.example.cleandata.utils.IpUtils;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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


//        String a1 = "10.0.0.0";
//        String a2 = "10.255.255.255";
//        String b1 = "172.16.0.0";
//        String b2 = "172.31.255.255";
//        String c1 = "192.168.0.0";
//        String c2 = "192.168.255.255";
//        String d = "127.0.0.1";
//        System.out.println(IpUtils.ipv4ToLong(a1));
//        System.out.println(IpUtils.ipv4ToLong(a2));
//        System.out.println(IpUtils.ipv4ToLong(b1));
//        System.out.println(IpUtils.ipv4ToLong(b2));
//        System.out.println(IpUtils.ipv4ToLong(c1));
//        System.out.println(IpUtils.ipv4ToLong(c2));
//        System.out.println(IpUtils.ipv4ToLong(d));

        String s = "112.104.70.0";
        System.out.println(IpUtils.ipv4ToLong(s));



//        long min = 18927616L;
//        System.out.println(IpUtils.ipv4ToStr(min));
//        long max = 3752136703L;
//        System.out.println(IpUtils.ipv4ToStr(max));

    }

}
