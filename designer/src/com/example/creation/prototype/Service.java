package com.example.creation.prototype;

public class Service implements Cloneable {

    private String s;
    private StringBuffer sb;

    Service(String s, StringBuffer sb) {
        this.s = s;
        this.sb = sb;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }

    public String getS() {
        return s;
    }

    public StringBuffer getSb() {
        return sb;
    }

    @Override
    protected Object clone() {
        Service service = null;
        try {
            service = (Service) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return service;
    }


}
