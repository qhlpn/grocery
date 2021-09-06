package com.example.provider.controller;


import com.example.provider.service.HystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@RestController
public class AppController {


    @Value("${server.port}")
    private int port;

    @Resource
    private HystrixService hystrixService;

    @GetMapping("/provider")
    public String get() {
//        return port + " : " + UUID.randomUUID().toString();
        return port + " : " + hystrixService.doService();
    }


}
