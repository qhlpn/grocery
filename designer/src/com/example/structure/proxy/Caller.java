package com.example.structure.proxy;

import java.lang.reflect.Proxy;

public class Caller {

    private Service serviceImpl = new ServiceImpl();
    private ServiceProxyInvocationHandler spih = new ServiceProxyInvocationHandler(serviceImpl);
    private Service serviceProxy = (Service) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), ServiceImpl.class.getInterfaces(), spih);


}
