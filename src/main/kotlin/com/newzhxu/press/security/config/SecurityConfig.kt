package com.newzhxu.press.security.config

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
        authenticationManager: AuthenticationManager,
    ): SecurityFilterChain {
        val filterChain = http
            .authorizeHttpRequests {
                it.requestMatchers(
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
            }
            .httpBasic {
            }
            .logout {
                it.disable()
            }
            .authenticationManager(authenticationManager)
            .exceptionHandling {
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
        defaultPasswordEncoder: PasswordEncoder
    ): DaoAuthenticationProvider {
        return DaoAuthenticationProvider(userDetailsService)
            .apply {
                this.setPasswordEncoder(defaultPasswordEncoder)
            }
    }

    @Bean
    fun authenticationManager(
        daoAuthenticationProvider: DaoAuthenticationProvider
    ): AuthenticationManager {
        return ProviderManager(daoAuthenticationProvider)
    }
}



