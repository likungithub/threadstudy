package com.example.demo.signal;

import java.util.concurrent.CountDownLatch;

/**
 * 利用对象的 wait和notify机制(downLatch.countDown();来决定线程顺序，一般不使用优先级线程)
 */
public class ThreadSignal_WaitNotify {

    private static volatile boolean t1Started = false;

    public static void main(String[] args) {
        final Object o = new Object();

        CountDownLatch downLatch = new CountDownLatch(1);

        char[] charsA = "ABCDEFG".toCharArray();
        char[] charsB = "1234567".toCharArray();



        new Thread(() -> {
            synchronized (o){
                for (char c : charsA){
                    System.out.print(c);
                    t1Started = true;
                    //downLatch.countDown();
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }

        },"t1").start();

        new Thread(()-> {
            /*try {
                downLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            synchronized (o){

                while (!t1Started){
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (char c : charsB){
                    System.out.print(c);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        },"t2").start();

    }

}
