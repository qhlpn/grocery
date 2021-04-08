package com.example.structure.adaptor.adaptor;

import com.example.structure.adaptor.service.V2Service;

public class V2AdaptorImpl implements Adaptor {
    @Override
    public boolean support(Object service) {
        return service instanceof V2Service;
    }

    @Override
    public void dohandle(Object service) {
        if (service instanceof V2Service) ((V2Service) service).v2Service();
    }
}
