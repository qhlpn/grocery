package com.example.creation.factory.common;

public class ServiceFactory {

    public Service createService(ServiceEnum serviceEnum) {

        if (ServiceEnum.Imp_V1 == serviceEnum) return new ServiceImpV1();
        if (ServiceEnum.Imp_V2 == serviceEnum) return new ServiceImpV2();
        return null;

    }


    public enum ServiceEnum {

        Imp_V1("1", "服务实现V1"),
        Imp_V2("2", "服务实现V2");

        private String code;
        private String desc;

        ServiceEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }




}

