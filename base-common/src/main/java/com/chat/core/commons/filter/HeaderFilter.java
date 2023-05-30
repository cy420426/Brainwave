package com.chat.core.commons.filter;

import com.chat.core.commons.context.BusinessStartTimeContext;
import com.chat.core.commons.id.IdGenerator;
import com.chat.core.log.constant.TraceLogConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * @classDesc: 功能描述:(HeaderFilter)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Configuration
@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class HeaderFilter implements WebMvcConfigurer {
    Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String url = request.getRequestURL().toString();
                //获取traceId
                String traceId = request.getHeader(TraceLogConstant.TRACE_ID);
                //如果获取不到traceId，则尝试主动生成一个ID
                if (StringUtils.isEmpty(traceId)) {
                    traceId = IdGenerator.ins().generator().toString();
                }
                MDC.put(TraceLogConstant.TRACE_ID, traceId);
                log.info("HeaderFilter.request,url={},traceId={}", url, traceId);

                //获取业务发生时间
                String time = request.getHeader(BusinessStartTimeContext.BUSINESS_START_TIME_HEADER_NAME);
                if (!StringUtils.isEmpty(time)) {
                    time = new String(decoder.decode(time));
                    BusinessStartTimeContext.mock(time);
                    log.info("HeaderFilter.start.time.header,startTime={}", time);
                } else {
                    debug("HeaderFilter.time.header.get.null,url={}", url);
                }

                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                debug("HeaderFilter.Context.clear");
                TraceLogConstant.clearTraceId();
                BusinessStartTimeContext.clear();
            }
        });
    }

    private void debug(String msg, Object... param) {
        if (log.isDebugEnabled()) {
            log.debug(msg, param);
        }
    }
}
