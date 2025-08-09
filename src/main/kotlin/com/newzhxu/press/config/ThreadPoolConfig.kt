package com.newzhxu.press.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
@EnableAsync
class ThreadPoolConfig {
    @Bean
    fun asyncExecutor(): Executor {
        return Executors.newFixedThreadPool(10) // Example: 10 threads
    }

    @Bean
    fun defaultAsyncConfigurer(asyncExecutor: Executor): AsyncConfigurer {
        return AsyncConfigurer(asyncExecutor)
    }
}

class AsyncConfigurer(val executor: Executor) : AsyncConfigurer {
    val logger: Logger = LoggerFactory.getLogger(AsyncConfigurer::class.java)
    override fun getAsyncExecutor(): Executor {
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return AsyncUncaughtExceptionHandler { throwable, method, vararg ->
            logger.error("Async exception occurred,method:{}", method, throwable)
        }
    }
}
