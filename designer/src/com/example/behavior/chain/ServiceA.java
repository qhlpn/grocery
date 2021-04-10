package com.example.behavior.chain;

public class ServiceA extends Service {

    ServiceA(Service service) {
        this.successor = service;
    }

    @Override
    public void service(String msg) {
        if ("A".equals(msg)) {
            doService();
        } else {
            successor.service(msg);
        }
    }

    @Override
    public void doService() {
        System.out.println("ServiceA");
    }
}
