package com.newzhxu.press

import com.newzhxu.press.config.PressProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(PressProperties::class)

class PressApplication


fun main(args: Array<String>) {
    runApplication<PressApplication>(*args)
}

