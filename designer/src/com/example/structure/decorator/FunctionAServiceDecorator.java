package com.example.structure.decorator;

public class FunctionAServiceDecorator implements FunctionA, Service {

    private Service service;

    FunctionAServiceDecorator(Service service) {
        this.service = service;
    }

    @Override
    public void funcA() {

    }

    @Override
    public void doService() {
        funcA();
        service.doService();
    }
}
