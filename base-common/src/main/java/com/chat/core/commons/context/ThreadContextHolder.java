package com.chat.core.commons.context;

import com.chat.core.log.constant.TraceLogConstant;
import com.chat.core.log.context.TraceLogApiInfoContext;
import com.chat.core.log.entity.ApiInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class ThreadContextHolder {

    private String businessTime;
    private String traceId;
    private ApiInfo apiInfo;

    /**
     * 捕获当前线程上下文信息
     *
     * @return ThreadContextHolder .
     */
    public static ThreadContextHolder capture() {
        ThreadContextHolder holder = new ThreadContextHolder();
        holder.businessTime = BusinessStartTimeContext.getTimeStr();
        holder.traceId = TraceLogConstant.getTraceId();
        holder.apiInfo = TraceLogApiInfoContext.get();
        return holder;
    }

    /**
     * 清理当前线程上下文内容
     */
    public static void clear() {
        BusinessStartTimeContext.clear();
        TraceLogApiInfoContext.reset();
        TraceLogConstant.clearTraceId();
    }

    /**
     * 注入到新线程上下文中
     */
    public void inject() {
        BusinessStartTimeContext.mock(this.businessTime);
        TraceLogApiInfoContext.set(this.apiInfo);
        TraceLogConstant.setTraceId(this.traceId);
    }

}
