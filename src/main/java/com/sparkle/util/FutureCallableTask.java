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
        String param = "test,a,b";
        // 定义任务:
        Callable<String> task = () -> {
            String result = param.replace(",", "|");
            return result + "1";
        };
        Object result = executor.submit(task).get();
        System.out.println(result);
    }
}