package com.example.demo.signal;

/**
 * 利用枚举变量来进行通讯（该变量必须保证各个线程的可见性）
 *
 */
public class ThreadSignal_Cas {

    enum ReadyToRun{T1,T2}

    static volatile ReadyToRun r = ReadyToRun.T1;

    public static void main(String[] args) {
        char[] charsA = "ABCDEFG".toCharArray();
        char[] charsB = "1234567".toCharArray();

        new Thread(() -> {
            for (char c : charsA){
                while (r != ReadyToRun.T1){}
                System.out.print(c);
                r = ReadyToRun.T2;
            }
        },"t1").start();

        new Thread(()-> {
            for (char c : charsB){
                while (r != ReadyToRun.T2){}
                System.out.print(c);
                r = ReadyToRun.T1;
            }


        },"t2").start();

    }


}
