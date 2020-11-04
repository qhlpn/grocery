package com.example.cleandata.config;


import com.example.cleandata.utils.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QiuHongLong
 */
@Configuration
public class CommonConfig {


    @Bean
    public SnowFlake snowFlake() {
        long maxWorkerId = ~(-1L << 5);
        long maxDatacenterId = ~(-1L << 5);
        long maxSequenceId = ~(-1L << 12);
        long t = System.currentTimeMillis();
        return new SnowFlake(1, 1, 1);
    }
}
