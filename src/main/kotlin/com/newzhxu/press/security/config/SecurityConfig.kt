package com.newzhxu.press.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.newzhxu.press.common.failure
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * @author zheng2580369@gmail.com
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig() {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        objectMapper: ObjectMapper,
        authenticationManager: AuthenticationManager
    ): SecurityFilterChain {
        val filterChain = http
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/", "/login", "/public/**", "/error",
                        "/user/register",
                        "/swagger-ui.html", "/swagger-ui.html/**", "/swagger-ui/**", "/v3/api-docs/**"
                    ).permitAll()
                    .requestMatchers("/dns/**").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().authenticated()
            }
            .csrf {
                it.disable() // Disable CSRF for simplicity, consider enabling in production
            }
            .formLogin {
                it.disable() // Disable form login
            }
            .httpBasic {
                it.authenticationEntryPoint { _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write(objectMapper.writeValueAsString(failure<Void>()))
                }

            }
            .logout {
                it.disable()
            }
            .authenticationManager(authenticationManager)
            .exceptionHandling {
                it.accessDeniedHandler { _, response, _ ->
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.writer.write(objectMapper.writeValueAsString(failure<Void>()))
                }

                it.authenticationEntryPoint { _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write(objectMapper.writeValueAsString(failure<Void>()))
                }


            }
            .build()
        return filterChain
    }

    @Bean
    fun delegatingPasswordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }


    @Bean
    fun daoAuthenticationProvider(
        userDetailsService: UserDetailsService,
    ): DaoAuthenticationProvider {
        return DaoAuthenticationProvider(userDetailsService)
    }

    @Bean
    fun authenticationManager(
        daoAuthenticationProvider: DaoAuthenticationProvider
    ): AuthenticationManager {
        return ProviderManager(daoAuthenticationProvider)
    }
}

fun main() {
    val encode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("112")
    println(encode)
}

