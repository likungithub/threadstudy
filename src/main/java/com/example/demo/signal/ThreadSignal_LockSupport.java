package com.example.demo.signal;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程之间的通信机制  交替打印字母和数字
 * LockSupport是一个线程阻塞工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，当然阻塞之后肯定得有唤醒的方法。
 * park和unpark可以实现类似wait和notify的功能，但是并不和wait和notify交叉，也就是说unpark不会对wait起作用，notify也不会对park起作用。
 * park和unpark的使用不会出现死锁的情况
 * blocker的作用是在dump线程的时候看到阻塞对象的信息
 */
public class ThreadSignal_LockSupport {

    static Thread t1 = null,t2 = null;

    public static void main(String[] args) {
        char[] charsA = "ABCDEFG".toCharArray();
        char[] charsB = "1234567".toCharArray();

        t1 = new Thread(() -> {
            for (char c : charsA){
                System.out.print(c);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        },"t1");

        t2 = new Thread(()-> {
            for (char c : charsB){
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(t1);
            }


        },"t2");

        t1.start();
        t2.start();

        //System.out.println();

    }


}
