package com.example.behavior.strategy;

public class ServiceImplB implements Service{

    @Override
    public boolean compare(Object l, Object r) {
        return (int) l < (int) r;
    }

}
