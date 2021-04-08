package com.example.structure.adaptor;

import com.example.structure.adaptor.adaptor.Adaptor;
import com.example.structure.adaptor.adaptor.V1AdaptorImpl;
import com.example.structure.adaptor.adaptor.V2AdaptorImpl;

import java.util.Arrays;
import java.util.List;

public class Server {

    private static List<Adaptor> adaptors = Arrays.asList(new V1AdaptorImpl(), new V2AdaptorImpl());

    public void doService(String className) {

        try {

            Object service = Class.forName(className).newInstance();
            Adaptor serviceAdaptor = null;
            for (Adaptor adaptor : adaptors) {
                if (adaptor.support(service)) {
                    serviceAdaptor = adaptor;
                    break;
                }
            }
            serviceAdaptor.dohandle(service);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
