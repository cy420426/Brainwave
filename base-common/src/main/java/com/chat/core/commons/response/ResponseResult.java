package com.chat.core.commons.response;

import com.alibaba.fastjson.JSON;
import com.chat.core.commons.code.BasicResponseCode;
import com.chat.core.commons.code.ElementaryResponseCode;
import com.chat.core.commons.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;

/**
 * @classDesc: 功能描述:(ResponseResult)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
@Data
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -5858147992383769655L;

    /**
     * 标记是否成功
     */
    private boolean success;
    /**
     * 业务码
     */
    private String code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 全链路跟踪ID
     */
    private String traceId;
    /**
     * 异常名称，无异常为空
     */
    private String exceptionName;
    /**
     * 异常消息
     */
    private String exceptionMsg;

    private static final String REPLACE_STR = "$";
    private static final String CODE_SPLIT = "@";
    private static final String NO_PASS = "1";

    public boolean getSuccess() {
        return success;
    }

    /**
     * 是否调用成功
     *
     * @return
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * 构建错误信息
     *
     * @param code
     * @param data
     * @return
     */
    private static <T> ResponseResult<T> build(BasicResponseCode code, T data, String[] params) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setSuccess(code.getCode().equals(ElementaryResponseCode.SUCCESS.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        if (StringUtils.isEmpty(code.prefix())) {
            result.setCode(code.getCode());
        } else {
            result.setCode(code.prefix() + CODE_SPLIT + code.getCode());
        }
        String msg = code.getMsg();
        //如果包含占位符
        if (msg.contains(REPLACE_STR) && params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                param = Matcher.quoteReplacement(param);
                msg = msg.replaceAll("\\$" + (i + 1), param);
            }
        }
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 判断是否为业务异常
     *
     * @return
     */
    public boolean isBusinessException() {
        return BusinessException.class.getSimpleName().equals(getExceptionName());
    }

    /**
     * 重新构造消息
     *
     * @param msg
     * @return
     */
    public ResponseResult<T> rebuildMsg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public ResponseResult<T> buildExceptionMsg(String msg) {
        this.exceptionMsg = msg;
        return this;
    }

    /**
     * 根据返回对象获取errCode
     *
     * @return
     */
    public BasicResponseCode convert() {

        String[] split = this.getCode().split(CODE_SPLIT);
        String prefix = "";
        String code = "";
        int length = 1;
        if (split.length == length) {
            code = split[0];
            prefix = "";
        }
        length = 2;
        if (split.length == length) {
            code = split[0];
            prefix = split[1];
        }
        String message = this.getMsg();

        String finalPrefix = prefix;
        String finalCode = code;
        return new BasicResponseCode() {
            @Override
            public String prefix() {
                return finalPrefix;
            }

            @Override
            public String getCode() {
                return finalCode;
            }

            @Override
            public String getMsg() {
                return message;
            }
        };
    }

    /**
     * 获取成功的结果
     *
     * @return
     */
    public static <T> ResponseResult<T> success() {
        return build(ElementaryResponseCode.SUCCESS, null, null);
    }

    /**
     * 获取 成功结果
     *
     * @param data 需要返回的数据
     * @return
     */
    public static <T> ResponseResult<T> success(T data) {
        return build(ElementaryResponseCode.SUCCESS, data, null);
    }

    /**
     * 获取失败的结果
     *
     * @return
     */
    public static <T> ResponseResult<T> error() {
        return build(ElementaryResponseCode.SYSTEM_ERROR, null, null);
    }

    /**
     * 获取失败的结果
     *
     * @param code 业务code
     * @return
     */
    public static <T> ResponseResult<T> error(BasicResponseCode code, String... params) {
        return build(code, null, params);
    }


    /**
     * 获取失败的结果
     *
     * @param code 业务code
     * @return
     */
    public static <T> ResponseResult<T> error(T data, BasicResponseCode code, String... params) {
        return build(code, data, params);
    }


    /**
     * 返回熔断结果
     *
     * @param throwable
     * @return
     */
    public static <T> ResponseResult<T> fallback(Throwable throwable) {
        log.error("ResponseResult.fallback,e=", throwable);
        return build(ElementaryResponseCode.FALLBACK, null, new String[]{throwable.getMessage()});
    }

    /**
     * 获取业务异常的ErrorCode
     *
     * @return
     */
    public BasicResponseCode convertErrorCode() {
        if (isBusinessException()) {
            String code = getCode();
            return new BasicResponseCode() {
                @Override
                public String prefix() {
                    return "BusinessExceptionErrorCode";
                }

                @Override
                public String getCode() {
                    return code;
                }

                @Override
                public String getMsg() {
                    return getMsg();
                }
            };
        }
        return convert();
    }
}
