package com.chat.core.commons.code;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public interface BasicResponseCode {

    /**
     * 设置前缀
     *
     * @return
     */
    String prefix();

    /**
     * 获取Code
     *
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     *
     * @return
     */
    String getMsg();

}
