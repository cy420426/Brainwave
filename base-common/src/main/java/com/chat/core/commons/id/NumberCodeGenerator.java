package com.chat.core.commons.id;

import cn.hutool.core.util.RandomUtil;
import com.chat.core.commons.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @classDesc: 功能描述:(单号生成)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class NumberCodeGenerator {

    private static final String SPLIT = "";

    public static class NumberCodeGeneratorHolder {
        public static final NumberCodeGenerator HOLDER = new NumberCodeGenerator();
    }

    public static NumberCodeGenerator ins() {
        return NumberCodeGeneratorHolder.HOLDER;
    }


    /**
     * 生成单号，分布式场景下需要主动判重
     *
     * @param prefix
     * @return
     */
    public String generator(String prefix) {
        StringBuilder builder = new StringBuilder(prefix);
        String dateStr = DateUtil.getDateStr(DateUtil.getNowDate(), "yyyyMMddHHmmssSSS");
        String random = RandomUtil.randomNumbers(6);
        builder.append(SPLIT).append(dateStr).append(SPLIT).append(random);
        return builder.toString();
    }

    public static void main(String[] args) {

        int length = 100;
        for (int i = 0; i < length; i++) {
            String id = NumberCodeGenerator.ins().generator("test");
            System.out.println("id=" + id);
        }


    }
}
