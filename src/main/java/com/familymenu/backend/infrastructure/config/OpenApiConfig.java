package com.familymenu.backend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info apiInfo = new Info()
                .title("Family Menu API")
                .description("API для страв, продуктів, меню та списків покупок.")
                .version("v1");

        return new OpenAPI().info(apiInfo);
    }
}