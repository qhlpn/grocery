package com.example.behavior.template;

public class Caller {

    private static Service sA = new ServiceImplA();
    private static Service sB = new ServiceImplB();

    public static void main(String[] args) {
        sA.doService();
        sB.doService();
    }

}
