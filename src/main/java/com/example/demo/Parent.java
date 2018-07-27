package com.example.demo;

/**
 * @author zhaowb
 * @create 2018-07-18 21:33
 * 描述:
 */
public class Parent {

    static {
        System.out.println("parent static");
    }

    public Parent() {
        System.out.println("parent ");
    }
    public void hello (){
        System.out.println("parent hello");
    }

}