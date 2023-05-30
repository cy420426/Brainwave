package com.chat.core.commons.id;

/**
 * @classDesc: 功能描述:(业务编码生成器)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class BusinessCodeGenerator {


    private static final String SPLIT = "_";

    public static class BusinessCodeGeneratorHolder {
        public static final BusinessCodeGenerator HOLDER = new BusinessCodeGenerator();
    }

    public static BusinessCodeGenerator ins() {
        return BusinessCodeGeneratorHolder.HOLDER;
    }


    public String generator(String prefix) {
        Long generator = IdGenerator.ins().generator();
        StringBuilder builder = new StringBuilder(prefix);
        builder.append(SPLIT).append(generator);
        return builder.toString();
    }

    public static void main(String[] args) {

        int length = 100;
        for (int i = 0; i < length; i++) {
            String id = BusinessCodeGenerator.ins().generator("test");
            System.out.println("BusinessCodeGenerator.main.[id]="+id);
        }

    }

}
