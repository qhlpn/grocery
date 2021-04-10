package com.example.behavior.strategy;

public class ServiceImplA implements Service{

    @Override
    public boolean compare(Object l, Object r) {
        return (int) l > (int) r;
    }

}
