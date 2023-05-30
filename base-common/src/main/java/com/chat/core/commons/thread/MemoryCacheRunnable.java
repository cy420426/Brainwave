package com.chat.core.commons.thread;

import org.springframework.stereotype.Component;

/**
 * @classDesc: 功能描述:(jmm共享内存线程)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Component
public interface MemoryCacheRunnable extends Runnable {

    /**
     * 是否独立线程运行 默认共享线程
     *
     * @return
     */
    boolean standalone();

    /**
     * 是否开启 默认关闭
     *
     * @return
     */
    boolean open();

    /**
     * 间隔运行时间 秒
     *
     * @return
     */
    int interval();

}
