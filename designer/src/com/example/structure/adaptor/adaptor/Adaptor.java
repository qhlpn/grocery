package com.example.structure.adaptor.adaptor;

public interface Adaptor {

    boolean support(Object service);

    void dohandle(Object service);
}
