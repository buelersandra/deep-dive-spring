package com.sika.contact.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
        .info(
                new Info()
                    .title("SIKA BOX INC User Management")
                    .description("API documentation for SIKA BOX INC")
                    .version("v1.0")
                    .termsOfService("TOC")
                    .license(new License().name("License").url("#"))
            );
    }
}
