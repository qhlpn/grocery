package com.example.behavior.chain;

public abstract class Service {

    protected Service successor;

    public abstract void service(String msg);

    public abstract void doService();


}
