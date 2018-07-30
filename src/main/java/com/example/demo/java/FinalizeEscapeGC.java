package com.example.demo.java;

public class FinalizeEscapeGC {

    private static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive(){
        System.out.println("yes ,i am still alive.");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws Exception{
        SAVE_HOOK = new FinalizeEscapeGC();

        // 对象第一次成功拯救自己
        SAVE_HOOK = null;
        System.gc();

        Thread.sleep(500);

        if (SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no ,i am dead!");
        }

        // 对象第一次成功拯救自己
        SAVE_HOOK = null;
        System.gc();

        Thread.sleep(500);

        if (SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("no ,i am dead!");
        }
    }
}
