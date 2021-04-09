package com.example.structure.flyweight;

public class Service {

    private final int primaryKey;
    private String msg;

    Service(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getMsg() {
        return msg;
    }


}
