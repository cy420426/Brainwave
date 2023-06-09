package com.chat.core.log.context;

import com.chat.core.log.entity.ApiInfo;
import org.springframework.core.NamedThreadLocal;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class TraceLogApiInfoContext {


    public static final String TRACE_HEADER_NAME = "traceApiInfo";

    private static final ThreadLocal<ApiInfo> INHERITABLE_CONTEXT_HOLDER = new NamedThreadLocal<>("trace-log-api-context");

    /**
     * 重置当前线程的数据信息
     */
    public static void reset() {
        INHERITABLE_CONTEXT_HOLDER.remove();
    }

    /**
     * 将traceId设置到线程上
     *
     * @param apiInfo 跟踪id
     */
    public static void set(ApiInfo apiInfo) {
        INHERITABLE_CONTEXT_HOLDER.set(apiInfo);
    }

    /**
     * 从线程的上下文中获取traceId
     *
     * @return
     */
    public static ApiInfo get() {
        return INHERITABLE_CONTEXT_HOLDER.get();
    }
}
