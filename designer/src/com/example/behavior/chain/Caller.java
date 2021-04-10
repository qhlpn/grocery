package com.example.behavior.chain;

public class Caller {

    private static Server server = new Server();

    public static void main(String[] args) {

        server.doService("E");

    }

}
