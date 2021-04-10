package com.example.behavior.strategy;


public class Caller {

    private static Server server = new Server();

    public static void main(String[] args) {
        Integer[] array = new Integer[]{1, 3, 5, 7, 2, 4, 6, 8};
//        server.sort(array, (l, r) -> (Integer)l < (Integer) r);
        server.sort(array, new ServiceImplB());
        System.out.println();
    }


}
