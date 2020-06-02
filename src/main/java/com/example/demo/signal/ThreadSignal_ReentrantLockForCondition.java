package com.example.demo.signal;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用ReentrantLock的Condition来控制线程之间的通信
 */
public class ThreadSignal_ReentrantLockForCondition {

    public static void main(String[] args) {
        char[] charsA = "ABCDEFG".toCharArray();
        char[] charsB = "1234567".toCharArray();

        Lock lock = new ReentrantLock();
        Condition conditionT1 = lock.newCondition();
        Condition conditionT2 = lock.newCondition();

        new Thread(() -> {

            try {
                lock.lock();
                for (char c : charsA) {
                    System.out.print(c);
                    conditionT2.signal();
                    conditionT1.await();
                }
                conditionT2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                lock.lock();
                for (char c : charsB) {
                    System.out.print(c);
                    conditionT1.signal();
                    conditionT2.await();


                }
                conditionT1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }, "t2").start();

    }

}
