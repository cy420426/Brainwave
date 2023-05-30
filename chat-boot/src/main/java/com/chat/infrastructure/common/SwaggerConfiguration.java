package com.chat.infrastructure.common;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author haoxr
 * @Date 2021/02/25
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {
    private String groupName = "chat";
    private String title = "在线API文档";
    private String version = "1.0.0";

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .build();
    }

}
