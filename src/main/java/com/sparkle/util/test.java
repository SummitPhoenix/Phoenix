package com.sparkle.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/11/27 下午3:29
 */
public class test {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1) );

    public static void main(String[] args) {

        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();
        threadFactory.newThread(()->{
            try {
                Thread.sleep(1000);
                System.out.println("ThreadPool");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
