package com.mjj.wxdemoreminds.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {

    private static final Map<String, Object> cache = new HashMap<>();

    private static Byte lock = 1;

    public static void set(String k, Object v) {
        synchronized (lock) {
            cache.put(k, v);
        }
    }

    public static Object get(String k) {
        return cache.get(k);
    }

    public static Object get(String k, Object defaultV) {
        return cache.getOrDefault(k, defaultV);
    }

}
