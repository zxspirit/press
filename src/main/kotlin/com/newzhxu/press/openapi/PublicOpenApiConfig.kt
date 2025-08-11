package com.newzhxu.press.openapi

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
class PublicOpenApiConfig {
    @Bean
    fun openapi(): OpenAPI {
        return OpenAPI().apply {
            info = Info().apply {
                title = "Press API"
                version = "1.0.0"
                description = "API documentation for Press application"
            }
            servers = listOf(
                Server().apply {
                    url = "http://localhost:8080"
                    description = "Local server"
                })
            security = listOf(
                SecurityRequirement().apply {
                    addList("basicAuth")
                    addList("formLogin")
                }
            )
            components = Components().apply {
                securitySchemes = mutableMapOf("basicAuth" to SecurityScheme().apply {
                    type = SecurityScheme.Type.HTTP
                    scheme = "basic"
                    description = "Basic authentication for API access"

                }, "formLogin" to SecurityScheme().apply {
                    type = SecurityScheme.Type.APIKEY
                    `in` = SecurityScheme.In.HEADER
                    name = "Authorization"
                    description = "Form login authentication for API access"
                })
            }


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