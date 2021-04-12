package com.example.behavior.template;

public class ServiceImplB extends Service.ServiceDefault {

    @Override
    public void stepBDifferent() {
        System.out.println("ServiceImplB");
    }

    @Override
    public void stepCOptional() {
        System.out.println("ServiceImplB");
    }

}
