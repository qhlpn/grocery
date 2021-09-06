package com.example.consumer.controller;


import com.example.consumer.service.FeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")  // hystrix缺省兜底fallback
public class AppController {

    // private static final String URL = "http://localhost:9000";

    // 服务注册 使得 Consumer 可以直接调用服务名，而不用再关心地址和端口号
    // private static final String URL = "http://MICRO-PROVIDER";
    private static final String URL = "http://micro-provider";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private FeignService appService;


    @GetMapping("/consumer")
//    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
    @HystrixCommand(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
    public String get() {
//        return restTemplate.getForObject(URL + "/provider", String.class, (Object) null);
        return appService.get();
    }


    private void getRegisterInfo() {
        for (String service : discoveryClient.getServices()) {
            log.info(service);
        }
    }

    public String fallback() {
        return "timeout or except , consumer fallback";
    }

    public String defaultFallback() {
        return "timeout or except , consumer default fallback";
    }


}
