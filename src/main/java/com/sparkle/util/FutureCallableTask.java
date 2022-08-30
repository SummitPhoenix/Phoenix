package com.sparkle.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 0028
 */
public class FutureCallableTask {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String param = "test";
        // 定义任务:
        Callable<String> taskA = () -> {
            String result = param + "a";
            return result;
        };
        Callable<String> taskB = () -> {
            String result = param + "b";
            return result;
        };
        Object resultA = executor.submit(taskA).get();
        Object resultB = executor.submit(taskB).get();

        System.out.println(resultA);
        System.out.println(resultB);
        executor.shutdown();
    }
}