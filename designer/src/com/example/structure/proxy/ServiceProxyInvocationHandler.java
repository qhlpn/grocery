package com.example.structure.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServiceProxyInvocationHandler implements InvocationHandler {

    private Service service;

    ServiceProxyInvocationHandler(Service service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("start");
        Object invoke = method.invoke(service, args);
        System.out.println("end");
        return invoke;

    }



}
