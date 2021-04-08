package com.example.creation.factory.reflect;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServiceFactory {

    public Service createService(String serviceName) {
        Properties properties = ServiceProperties.getRegisterServices();
        Service service = null;
        try {
            service = (Service) Class.forName((String) properties.get(serviceName)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public static class ServiceProperties {

        public static Properties getRegisterServices() {
            Properties properties = new Properties();
            String relativePath = Thread.currentThread().getClass().getResource("/").getPath();
            // 编译后程序运行时相对路径
            // E:/projects/grocery/designer/out/production/designer/
            try (FileInputStream fis = new FileInputStream(relativePath + "com/example/creation/factory/reflect/service.properties")) {
                properties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return properties;
        }

    }





}

