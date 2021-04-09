package com.example.creation.factory.reflect;

public class Caller {

    private static ServiceFactory factory = new ServiceFactory();

    public static void main(String[] args) {

        // 实现了创建者（工厂）和调用者（main）之间解耦
        // 从而后续只需维护工厂的对象生成细节，调用者无需改动

        // 反射工厂比起普通工厂，统一了对象生产流程（类名、初始化的原材料）
        // 无需维护工厂代码，只需维护配置文件
        Service v1 = factory.createService("service.v1");
        Service v2 = factory.createService("service.v2");

    }

}
