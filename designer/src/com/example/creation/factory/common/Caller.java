package com.example.creation.factory.common;

public class Caller {

    private static ServiceFactory factory = new ServiceFactory();

    public static void main(String[] args) {

        // 实现了创建者（工厂）和 调用者 之间解耦
        // 从而后续只需维护工厂的对象生成细节，调用者无需改动
        Service v1 = factory.createService(ServiceFactory.ServiceEnum.Imp_V1);
        Service v2 = factory.createService(ServiceFactory.ServiceEnum.Imp_V2);
    }

}
