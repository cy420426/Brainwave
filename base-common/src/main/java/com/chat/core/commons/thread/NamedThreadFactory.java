package com.chat.core.commons.thread;

import java.util.concurrent.ThreadFactory;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class NamedThreadFactory implements ThreadFactory {

    private String name;

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {

        Thread thread = new Thread(r);
        thread.setName(name);
        thread.setDaemon(false);

        return thread;
    }
}
