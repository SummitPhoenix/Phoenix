package com.sparkle.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Smartisan
 */
public class ThreadPoolUtil {

    public static ThreadPoolExecutor getThreadPoolExecutor(String nameFormat) {
        //命名线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
        //初始化线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 30, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), namedThreadFactory);
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
