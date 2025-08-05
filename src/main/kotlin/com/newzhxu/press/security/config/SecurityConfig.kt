package com.newzhxu.press.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.newzhxu.press.security.filter.JwtFilter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(val jwtFilter: JwtFilter, val objectMapper: ObjectMapper) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val filterChain = http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/login", "/public/**", "/error").permitAll()
                    .requestMatchers("/dns/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().authenticated()
            }
            .csrf {
                it.disable() // Disable CSRF for simplicity, consider enabling in production
            }
            .formLogin {
                it.disable() // Disable form login
            }
            .httpBasic {
                it.disable() // Disable HTTP Basic authentication
            }
            .logout {
                it.disable()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.accessDeniedHandler { request, response, _ ->
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.writer.write("Access Denied")
                }
            }
            .build()
        return filterChain
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

