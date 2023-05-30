package com.chat.core.commons.advice;

import com.chat.core.commons.code.BasicResponseCode;
import com.chat.core.commons.code.error.AdviceErrorCode;
import com.chat.core.commons.exception.BusinessException;
import com.chat.core.commons.response.ResponseResult;
import com.chat.core.log.constant.TraceLogConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
@ConditionalOnProperty(prefix = "project.separate", value = {"enable"}, havingValue = "true", matchIfMissing = true)
@RestControllerAdvice
public class ExceptionAdvice {


    /**
     * hibernate validator 数据绑定验证异常拦截
     *
     * @param e 绑定验证异常
     * @return 错误返回消息
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public ResponseResult<?> validateErrorHandler(HttpServletRequest request, BindException e) {
        ObjectError error = e.getAllErrors().get(0);
        log.error("参数绑定异常(BindException),", e);
        ResponseResult<?> result = ResponseResult.error(AdviceErrorCode.BIND_EX, error.getDefaultMessage())
                .buildExceptionMsg(getExceptionStack(e));
        result.setExceptionName(e.getClass().getSimpleName());
        result.setTraceId(TraceLogConstant.getTraceId());
        return result;
    }

    /**
     * hibernate validator 数据绑定验证异常拦截
     *
     * @param e 绑定验证异常
     * @return 错误返回消息
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> validateErrorHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        ObjectError error = e.getBindingResult().getAllErrors().get(0);
        log.warn("参数校验错误(MethodArgumentNotValidException),", e);
        ResponseResult<?> result = ResponseResult.error(AdviceErrorCode.PARAM_NOT_VALID_EX, error.getDefaultMessage())
                .buildExceptionMsg(getExceptionStack(e));
        result.setExceptionName(e.getClass().getSimpleName());
        result.setTraceId(TraceLogConstant.getTraceId());
        return result;
    }

    /**
     * spring validator 方法参数验证异常拦截
     *
     * @param e 绑定验证异常
     * @return 错误返回消息
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> defaultErrorHandler(HttpServletRequest request, ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        log.warn("参数校验错误(ConstraintViolationException),", e);
        ResponseResult<?> result = ResponseResult.error(AdviceErrorCode.CONSTRAINT_EX, violation.getMessage())
                .buildExceptionMsg(getExceptionStack(e));
        result.setExceptionName(e.getClass().getSimpleName());
        result.setTraceId(TraceLogConstant.getTraceId());
        return result;
    }

    /**
     * 处理业务异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<?> handleBusinessException(HttpServletRequest request, BusinessException e) {
        log.warn("业务异常(BusinessException),", e);
        BasicResponseCode errorCode = new BasicResponseCode() {
            @Override
            public String prefix() {
                return e.getPrefix();
            }

            @Override
            public String getCode() {
                return e.getCode();
            }

            @Override
            public String getMsg() {
                return e.getMessage();
            }
        };

        ResponseResult<?> result = ResponseResult.error(errorCode)
                .buildExceptionMsg(getExceptionStack(e));
        result.setExceptionName(e.getClass().getSimpleName());
        result.setTraceId(TraceLogConstant.getTraceId());
        return result;
    }


    /**
     * 处理异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<?> handleException(HttpServletRequest request, Exception e) {
        log.error("通用异常 stack info:{}", getExceptionStack(e));
        ResponseResult<?> result = ResponseResult.error(AdviceErrorCode.OTHER_EX,"请稍后重试~");
        result.buildExceptionMsg(getExceptionStack(e));
        result.setExceptionName(e.getClass().getSimpleName());
        result.setTraceId(TraceLogConstant.getTraceId());
        return result;
    }


    public static String getExceptionStack(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }


}
