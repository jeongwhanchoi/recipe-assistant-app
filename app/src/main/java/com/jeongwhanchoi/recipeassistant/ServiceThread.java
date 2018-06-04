package com.jeongwhanchoi.recipeassistant;

import android.os.Handler;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    int sleepTime;
    MyService myService;


    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            try{

                sleepTime = 30000;
                Thread.sleep(sleepTime);
                isRun=false;
            }catch (Exception e) {}
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
        }
    }

}