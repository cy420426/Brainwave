package com.chat.core.commons.advice;

import com.chat.core.commons.annotations.NoResponseResult;
import com.chat.core.commons.code.error.AdviceErrorCode;
import com.chat.core.commons.response.ResponseResult;
import com.chat.core.log.constant.TraceLogConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@ConditionalOnProperty(prefix = "project.separate", value = {"enable"}, havingValue = "true", matchIfMissing = true)
@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice {


    private static final List<String> IGNORE_URL = Arrays.asList("swagger-resources", "api-docs", "actuator");

    private static final String HTTP_STATUS_CODE = "status";

    /**
     * 支持的类型：默认都支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !returnType.hasMethodAnnotation(NoResponseResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String url = httpRequest.getRequestURL().toString();
        boolean ignore = IGNORE_URL.stream().anyMatch(url::contains);
        if (ignore) {
            return body;
        }

        if (body == null) {
            ResponseResult<Object> success = ResponseResult.success();
            success.setTraceId(TraceLogConstant.getTraceId());
            return success;
        }

        if (body instanceof ResponseResult) {
            ResponseResult<?> result = (ResponseResult<?>) body;
            result.setTraceId(TraceLogConstant.getTraceId());
            return body;
        }
        if (body instanceof LinkedHashMap) {
            LinkedHashMap map = (LinkedHashMap) body;
            Integer sucessStatus = HttpStatus.OK.value();
            if (map.containsKey(HTTP_STATUS_CODE) && !sucessStatus.equals(map.get(HTTP_STATUS_CODE))) {
                ResponseResult<LinkedHashMap> error = ResponseResult.error(map, AdviceErrorCode.OTHER_EX);
                error.setTraceId(TraceLogConstant.getTraceId());
                return error;
            }
        }
        ResponseResult<Object> success = ResponseResult.success(body);
        success.setTraceId(TraceLogConstant.getTraceId());
        return success;
    }


}
