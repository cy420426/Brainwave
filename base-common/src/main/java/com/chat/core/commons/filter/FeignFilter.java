package com.chat.core.commons.filter;

import com.chat.core.commons.context.BusinessStartTimeContext;
import com.chat.core.commons.id.IdGenerator;
import com.chat.core.log.constant.TraceLogConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Optional;

/**
 * @classDesc: 功能描述:(FeignFilter)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Configuration
@ConditionalOnClass(FeignClient.class)
@Slf4j
public class FeignFilter implements RequestInterceptor {

    @Value("${spring.application.name}")
    public String upstreamServerName;
    private static final String CAST_APP = "Cast-App";
    private static final String CAST_CLIENT_IP = "Cast-Client-IP";
    private static final String CAST_DEVICE = "Cast-Device";
    private static final String CAST_TOKEN = "Cast-Token";


    Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        // 不为空时取出请求中的header 原封不动的设置到feign请求中
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            String castApp = request.getHeader(CAST_APP);
            String castClientIp = request.getHeader(CAST_CLIENT_IP);
            String castDevice = request.getHeader(CAST_DEVICE);
            String castToken = request.getHeader(CAST_TOKEN);
            Optional.ofNullable(castApp)
                    .ifPresent(e -> template.header(CAST_APP, e));
            Optional.ofNullable(castClientIp)
                    .ifPresent(e -> template.header(CAST_CLIENT_IP, e));
            Optional.ofNullable(castDevice)
                    .ifPresent(e -> template.header(CAST_DEVICE, e));
            Optional.ofNullable(castToken)
                    .ifPresent(e -> template.header(CAST_TOKEN, e));
        }
        String url = template.url();
        //从上下文中取出traceId
        String traceId = TraceLogConstant.getTraceId();
        if (traceId.equals(TraceLogConstant.NULL)) {
            traceId = IdGenerator.ins().generator().toString();
        }
        log.info("FeignFilter.target,url={},traceId={}", url, traceId);
        MDC.put(TraceLogConstant.TRACE_ID, traceId);
        template.header(TraceLogConstant.TRACE_ID, traceId);

        //从上下文中取出业务首次发生时间
        String time = BusinessStartTimeContext.getTimeStr();
        if (!StringUtils.isEmpty(time)) {
            log.info("FeignFilter.start.time.header,startTime={}", time);
            time = encoder.encodeToString(time.getBytes());
            template.header(BusinessStartTimeContext.BUSINESS_START_TIME_HEADER_NAME, time);
        } else {
            this.debug("FeignFilter.time.header.null,url={}", url);
        }
    }

    private void debug(String msg, Object... param) {
        if (log.isDebugEnabled()) {
            log.debug(msg, param);
        }
    }
}
