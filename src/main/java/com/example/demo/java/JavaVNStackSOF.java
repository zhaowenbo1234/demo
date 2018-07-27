package com.example.demo.java;

public class JavaVNStackSOF {

    private int stackLength = 1;

    public void stackLeak(){
        stackLength ++ ;
        stackLeak();
    }
    public static void main(String[] args) {

        JavaVNStackSOF oom = new JavaVNStackSOF();
        try{
            oom.stackLeak();
        }catch (Throwable e){

            System.out.println("Stack length :" + oom.stackLength);
            throw e;
        }
    }
}
