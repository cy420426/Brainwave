package com.chat.infrastructure.common.inceptor;

import com.chat.infrastructure.common.inceptor.CommonInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .excludePathPatterns(
                        "/user/login",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/doc.html",
                        "/swagger-ui.html/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 解决knife4j无法访问
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决spring boot admin无法访问问题
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/META-INF/spring-boot-admin-server-ui/assets/");
    }
}
