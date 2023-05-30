package com.chat.core.log.constant;

import org.slf4j.MDC;

/**
 * @classDesc: 功能描述:(TraceLogConstant)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public interface TraceLogConstant {
    /**
     * tracceId
     */
    String TRACE_ID = "X-MAP-TraceId";
    String NULL = "N/A";

    /**
     * 判断当前线程是否绑定了traceId
     *
     * @return
     */
    static boolean isBindTraceId() {
        return MDC.get(TRACE_ID) != null;
    }

    /**
     * 获取Mdc中的traceId
     *
     * @return
     */
    static String getTraceId() {
        String traceId = MDC.get(TRACE_ID);
        return traceId == null ? NULL : traceId;
    }

    /**
     * 项Mdc中设置traceId
     *
     * @param traceId
     */
    static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * clear
     */
    static void clearTraceId() {
        MDC.clear();
    }


}
