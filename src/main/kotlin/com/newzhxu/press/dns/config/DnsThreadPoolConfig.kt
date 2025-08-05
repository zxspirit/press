package com.newzhxu.press.dns.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
class DnsThreadPoolConfig {
    @Bean(destroyMethod = "shutdown")
    fun dnsThreadPool(): ExecutorService {
        return Executors.newFixedThreadPool(8)
    }
}