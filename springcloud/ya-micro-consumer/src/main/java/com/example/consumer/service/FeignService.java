package com.example.consumer.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;


@Component
@FeignClient(value = "micro-provider")
public interface FeignService {


    @GetMapping("/provider")
    String get();

}
