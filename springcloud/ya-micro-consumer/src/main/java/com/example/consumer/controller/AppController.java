package com.example.consumer.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@RestController
public class AppController {

    // private static final String URL = "http://localhost:9000";

    // 服务注册 使得 Consumer 可以直接调用服务名，而不用再关心地址和端口号
    // private static final String URL = "http://MICRO-PROVIDER";
    private static final String URL = "http://micro-provider";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;


    @GetMapping("/consumer")
    public String get() {
        return restTemplate.getForObject(URL + "/provider", String.class, (Object) null);
    }


    private void getRegisterInfo() {
        for (String service : discoveryClient.getServices()) {
            log.info(service);
        }
    }


}
