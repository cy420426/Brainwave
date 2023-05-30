package com.chat.core.commons.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@AllArgsConstructor
@Getter
public enum ElementaryResponseCode implements BasicResponseCode {

    /**
     * 成功
     */
    SUCCESS("0", "success"),
    /**
     * 系统熔断
     */
    FALLBACK("system@9998", "系统繁忙,$1"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR("system@9999", "系统错误,$1");


    private String code;
    private String msg;

    @Override
    public String prefix() {
        return "";
    }

}
