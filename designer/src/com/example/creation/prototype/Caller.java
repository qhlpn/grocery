package com.example.creation.prototype;

public class Caller {

    public static void main(String[] args) {

        Service p = new Service("String不可变类", new StringBuffer("StringBuilder对象"));
        System.out.println(p);

        Service p1 = (Service) p.clone();
        System.out.println(p1);

        String s = p1.getS();
        s = "给String重新赋值只是改变它引用，不会改变它本来内存地址上的值";
        StringBuffer sb = p.getSb();
        sb.append("clone是浅拷贝");

        System.out.println(p.getS());
        // "String不可变类"
        System.out.println(p.getSb().toString());
        // "StringBuilder对象clone是浅拷贝"


    }

}
