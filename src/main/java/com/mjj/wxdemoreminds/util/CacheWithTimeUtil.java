package com.mjj.wxdemoreminds.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class CacheWithTimeUtil {

    private static Map<String, Entry> cache = new HashMap<>(16);
    private static Byte lock = 1;

    public static void set(String k, Object v, long oof) {
        synchronized (lock) {
            cache.put(k, new Entry(v, oof));
        }
    }

    public static void set(String k, Object v) {
        synchronized (lock) {
            cache.put(k, new Entry(v));
        }
    }

    public static Object get(String k) {

        Entry entry = cache.get(k);
        if (entry == null || System.currentTimeMillis() - entry.ts > entry.oof) {
            cache.remove(k);
            return null;
        }
        return entry.v;
    }



    @Setter
    @Getter
    public static class Entry {
        private Object v;
        private long ts;
        private long oof;

        public Entry(Object v, long oof) {
            this.v = v;
            this.oof = oof;
            this.ts = System.currentTimeMillis();
        }

        public Entry(Object v) {
            this(v, 7200);
        }

    }
}
