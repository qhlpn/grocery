package com.example.structure.adaptor;

public class Caller {

    public static void main(String[] args) {

        // 不同的 service 有着不同的 方法名
        // 适配模式可在不改变 service 代码的情况下， 将不同的 service 方法名往上抽象一层，统一调用

        new Server().doService("com.example.structure.adaptor.service.V1Service");
        new Server().doService("com.example.structure.adaptor.service.V2Service");

    }

}
