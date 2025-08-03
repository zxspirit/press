package com.newzhxu.press.openapi

import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
class OpenApiConfig {
    @Bean
    fun openapi(): OpenAPI {
        return OpenAPI().apply {
            info = io.swagger.v3.oas.models.info.Info().apply {
                title = "Press API"
                version = "1.0.0"
                description = "API documentation for Press application"
            }
            servers = listOf(
                io.swagger.v3.oas.models.servers.Server().apply {
                    url = "http://localhost:8080"
                    description = "Local server"
                })
        }
    }

    @Bean
    fun dnsGroup(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("dns")
            .pathsToMatch("/dns/**")
            .build()
    }
}