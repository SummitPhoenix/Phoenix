package com.sparkle.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Smartisan
 */
@Component
public class DataCache {

    public static final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS)
            .recordStats()
            .build();

    public static void main(String[] args) throws ExecutionException {
        String key = "";
        String value = "";
        CACHE.put(key, value);
        CACHE.get(key, () -> null);
    }
}