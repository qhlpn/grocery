package com.example.behavior.chain;

public class Server {

    private static Service head = new ServiceA(new ServiceB(new ServiceC(null)));

    public void doService(String msg) {
        try {
            head.service(msg);
        } catch (NullPointerException e) {
            System.out.println("find no service support.");
        }
    }


}
