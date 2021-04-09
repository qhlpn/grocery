package com.example.structure.flyweight;

import java.util.HashMap;

public class ServiceFactory {

    private static HashMap<Integer, Service> map = new HashMap<>();

    public synchronized Service createService(int primaryKey) {

        if (map.containsKey(primaryKey))
            return map.get(primaryKey);
        Service service = new Service(primaryKey);
        map.put(primaryKey, service);
        return service;

    }


}
