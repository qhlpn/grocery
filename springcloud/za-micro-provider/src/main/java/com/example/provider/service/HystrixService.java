package com.example.provider.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HystrixService {


    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000")})
    public String doService() {
        try {
            log.info("sleep some time");
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int except = 1 / 0;
        return "provider do service";
    }


    public String fallback() {
        return "timeout or except , provider fallback";
    }

}
