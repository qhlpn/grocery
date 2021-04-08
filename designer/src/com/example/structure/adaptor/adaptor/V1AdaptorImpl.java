package com.example.structure.adaptor.adaptor;

import com.example.structure.adaptor.service.V1Service;

public class V1AdaptorImpl implements Adaptor {

    @Override
    public boolean support(Object service) {
        return service instanceof V1Service;
    }

    @Override
    public void dohandle(Object service) {
        if (service instanceof V1Service) ((V1Service) service).v1Service();
    }
}
