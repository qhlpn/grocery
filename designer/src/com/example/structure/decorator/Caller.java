package com.example.structure.decorator;

public class Caller {

    private Service service = new ServiceImpl();

    private FunctionA serviceFunctionA = new FunctionAServiceDecorator(service);

    private FunctionB serviceFunctionB = new FunctionBServiceDecorator(service);

}
