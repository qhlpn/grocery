package com.example.behavior.chain;

public class ServiceB extends Service {

    ServiceB(Service service) {
        this.successor = service;
    }

    @Override
    public void service(String msg) {
        if ("B".equals(msg)) {
            doService();
        } else {
            successor.service(msg);
        }
    }

    @Override
    public void doService() {

        System.out.println("ServiceB");

    }
}
