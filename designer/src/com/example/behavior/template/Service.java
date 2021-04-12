package com.example.behavior.template;

public interface Service {

    void doService();

    void stepACommon();

    void stepBDifferent();

    void stepCOptional();

    public abstract class ServiceDefault implements Service {

        @Override
        public final void doService() {
            // 若有固定的步骤，则可以定义模仿方法，以 final 修饰
            stepACommon();
            stepBDifferent();
            stepCOptional();
        }

        @Override
        public void stepACommon() {
            System.out.println("通用操作由抽象类实现，子类直接使用");
        }

        @Override
        public void stepBDifferent() {
            // 子类覆盖该方法，以实现不同的操作
        }

        
        @Override
        public void stepCOptional() {
            // 可选的方法，先由抽象类空初始化，子类视情况覆盖它
        }


    }



}
