package com.sparkle.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Smartisan
 */
public class ThreadPoolUtil {

    public static ThreadPoolExecutor getThreadPoolExecutor(String nameFormat) {
        //创建线程池工厂，附带线程命名规则
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
        //初始化线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 固定大小线程池
     */
    public static ThreadPoolExecutor newFixedThreadPool(int poolSize) {
        //创建线程池工厂，附带线程命名规则
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("data-collection-%d").build();
        //初始化线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    public static void main(String[] args) {
        //创建线程池
        ThreadPoolExecutor executor = newFixedThreadPool(50);

        String companyName = "测试公司";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("companyName", companyName + i);
            Task task = new Task(param);
            executor.execute(task);
        }
        //关闭线程池
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                stopWatch.stop();
                System.out.println(stopWatch.getTotalTimeSeconds());
                break;
            }
        }
    }

    /**
     * 线程执行任务
     */
    static class Task implements Runnable {
        private final Map<String, Object> param;

        public Task(Map<String, Object> param) {
            this.param = param;
        }

        @Override
        public void run() {
            System.out.println("执行task [{}] - {}" + param + Thread.currentThread().getName());
        }
    }

}