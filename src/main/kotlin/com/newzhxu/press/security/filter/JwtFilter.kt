package com.newzhxu.press.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.newzhxu.press.config.PressProperties
import com.newzhxu.press.entity.failure
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author zheng2580369@gmail.com
 */
class JwtFilter(
    pressProperties: PressProperties,
    val userDetailsService: UserDetailsService,
    val delegatingAuthenticationEntryPoint: AuthenticationEntryPoint
) : OncePerRequestFilter() {
    private val authenticationConverter: AuthenticationConverter =
        JwtAuthenticationConverter(pressProperties.keyPairConfig.keyPair.public)
    private val securityContextHolderStrategy: SecurityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy()


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {


        try {
            val auth = this.authenticationConverter.convert(request) as JwtAuthenticationToken?
            if (auth == null) {
                logger.trace("Authentication not found in request, continuing filter chain")
                filterChain.doFilter(request, response)
                return
            }
            val details = userDetailsService.loadUserByUsername(auth.name)
            val authorities = auth.authorities as MutableList<GrantedAuthority>
            authorities.addAll(details.authorities)
            securityContextHolderStrategy.context.authentication = auth
        } catch (ex: AuthenticationException) {
            delegatingAuthenticationEntryPoint.commence(request, response, ex)
            return
        }
        filterChain.doFilter(request, response)


    }


}

class JwtAuthenticationEntryPoint(val objectMapper: ObjectMapper) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(failure<Void>()))

    }
}
