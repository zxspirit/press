package com.newzhxu.press.security.filter

import com.newzhxu.press.config.common.PressProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author zheng2580369@gmail.com
 */
@Component
class JwtFilter(
    val pressProperties: PressProperties
//    val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    private val authenticationConverter: AuthenticationConverter =
        JwtAuthenticationConverter(pressProperties.keyPairConfig.keyPair.public)
    private val securityContextHolderStrategy: SecurityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy()

    @Autowired
    lateinit var userDetailsService: UserDetailsService


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {


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



        filterChain.doFilter(request, response)


    }
}