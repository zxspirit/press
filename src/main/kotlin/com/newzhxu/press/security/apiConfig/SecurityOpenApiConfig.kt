package com.newzhxu.press.security.apiConfig

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
class SecurityOpenApiConfig {
    @Bean
    fun userGroupedApi(): GroupedOpenApi {

        return GroupedOpenApi.builder()
            .group("user")
            .pathsToMatch("/user/**")
            .build()
    }
}