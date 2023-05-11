package com.credio.credit.application.system.configuration

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {
    @Bean
    fun publicApi(): GroupedOpenApi{
        return GroupedOpenApi.builder()
            .group("spring-credit-application-public")
            .packagesToScan("com.credio.application.system.controller")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .addOpenApiCustomizer {openApi ->
                openApi.info(Info().title("Credit API").version("1.0.0"))
            }
            .build()
    }
}