package com.example.behavior.chain;

public class ServiceC extends Service {

    ServiceC(Service service) {
        this.successor = service;
    }

    @Override
    public void service(String msg) {
        if ("C".equals(msg)) {
            doService();
        } else {
            successor.service(msg);
        }
    }

    @Override
    public void doService() {
        System.out.println("ServiceC");
    }
}
