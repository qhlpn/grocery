package com.example.structure.proxy;

import java.lang.reflect.Proxy;

public class Caller {

    public static void main(String[] args) {

        ServiceProxyInvocationHandler spih = new ServiceProxyInvocationHandler(new ServiceImp());

        // classloader + interface[] + invocationHandler
        Service proxy1 = (Service) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), ServiceImp.class.getInterfaces(), spih);
        System.out.println(proxy1.run());
        Service proxy2 = (Service) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), ServiceImp.class.getInterfaces(), spih);
        System.out.println(proxy2.run());

    }

}
