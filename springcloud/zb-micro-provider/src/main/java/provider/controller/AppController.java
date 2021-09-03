package provider.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AppController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/provider")
    public String get() {
        return port + " : " + UUID.randomUUID().toString();
    }

}
