package com.chat.core.commons.sharding;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @classDesc: 功能描述:(线程分片)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class ThreadSharding {
    private static final ThreadConsistentHash THREAD_CONSISTENT_HASH = new ThreadConsistentHash(1800);
    private static final Map<String, ExecutorService> THREAD_MAP = new HashMap<>();
    private Integer threadSize = 15;

    public ThreadSharding() {
        init();
    }

    public ThreadSharding(Integer threadSize) {
        this.threadSize = threadSize;
        init();
    }

    private void init() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < threadSize; i++) {
            list.add("Thread-" + UUID.randomUUID().toString().replace("-", ""));
        }
        for (String s : list) {
            THREAD_CONSISTENT_HASH.addNode(s);
            THREAD_MAP.put(s, Executors.newSingleThreadExecutor());
        }
    }

    public ExecutorService strategyThread(String info) {
        String node = THREAD_CONSISTENT_HASH.getNode(info);
        return THREAD_MAP.get(node);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        List<String> res = new ArrayList<>();
        ThreadSharding threadSharding = new ThreadSharding();
        for (int i = 0; i < 1000; i++) {
            String node = threadSharding.strategyThread("hello" + i).toString();
            res.add(node);
        }
        Map<String, Integer> map = new HashMap<>(10);
        for (String item : res) {
            if (map.containsKey(item)) {
                map.put(item, map.get(item) + 1);
            } else {
                map.put(item, 1);
            }
        }
        for (String key : map.keySet()) {
            map.get(key);
            System.out.println(key + ":" + "命中数量" + map.get(key) + ", ");
        }
    }
}
