package com.example.structure.decorator;

public class FunctionBServiceDecorator implements FunctionB, Service {

    private Service service;

    FunctionBServiceDecorator(Service service) {
        this.service = service;
    }

    @Override
    public void funcB() {

    }

    @Override
    public void doService() {
        funcB();
        service.doService();
    }
}
