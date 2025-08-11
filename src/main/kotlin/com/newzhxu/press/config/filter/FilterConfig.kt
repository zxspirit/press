package com.newzhxu.press.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
class FilterConfig {

    @Bean
    fun loggingFilter(): FilterRegistrationBean<LoggingFilter> {
        return FilterRegistrationBean<LoggingFilter>().apply {
            this.filter = LoggingFilter()
            this.addUrlPatterns("/*") // Apply to all URLs

        }
    }
}