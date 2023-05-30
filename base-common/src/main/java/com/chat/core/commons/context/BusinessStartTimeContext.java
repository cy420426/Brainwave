package com.chat.core.commons.context;

import com.chat.core.commons.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

import java.util.Date;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class BusinessStartTimeContext {

    public static final String BUSINESS_START_TIME_HEADER_NAME = "start-time";

    /**
     * 线程上下文
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new NamedThreadLocal<>(BUSINESS_START_TIME_HEADER_NAME);

    /**
     * 获取String类型
     *
     * @return
     */
    public static String getTimeStr() {
        String time = CONTEXT_HOLDER.get();
        if (StringUtils.isEmpty(time)) {
            time = DateUtil.getNowStr();
            log.info("BusinessStartTimeContext.getTime.not.set.will.auto.fill,time={}", time);
            mock(time);
        }
        return time;
    }

    /**
     * 获取时间类型
     *
     * @return
     */
    public static Date getTime() {
        String time = getTimeStr();
        if (!StringUtils.isEmpty(time)) {
            return DateUtil.dateStrToDate(time);
        }
        return DateUtil.getNowDate();
    }


    /**
     * 模拟信息，测试使用
     *
     * @param time
     */
    public static void mock(String time) {
        if (!StringUtils.isEmpty(time)) {
            CONTEXT_HOLDER.set(time);
        }

    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
