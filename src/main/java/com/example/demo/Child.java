package com.example.demo;

/**
 * @author zhaowb
 * @create 2018-07-18 21:34
 * 描述:
 */
public class Child extends Parent {

    static {
        System.out.println("child static");
    }

    public Child() {
        System.out.println("child");
    }

    @Override
    public void hello (){
        System.out.println("child hello");
    }

    public static void main(String[] args) {
        Parent p = new Child();
        p.hello();
    }
}