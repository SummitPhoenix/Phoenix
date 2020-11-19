package com.sparkle.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author: XuanXiangHui
 * @Date: 2019/12/30 下午3:42
 */
@Component
public class DataCache {
    /**
     * key spaceId
     */
    public static Map<String, ArrayList<String>> photoList = new HashMap<>();


    private static final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .recordStats()
            .build();

    public static void main(String[] args) throws ExecutionException {
        String key = "";
        String value = "";
        CACHE.put(key, value);
        CACHE.get(key, () -> null);
    }
}
