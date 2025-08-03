package com.newzhxu.press.security.filter

import com.newzhxu.press.security.config.JwtConfig
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author zheng2580369@gmail.com
 */
@Component
class JwtFilter(
    val userDetailsService: UserDetailsService,
    val jwtConfig: JwtConfig
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader("Authorization")
        if (header.isNullOrEmpty() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        val token = header.removePrefix("Bearer ").trim()
        val userId = jwtConfig.getUserId(token)

        val userDetails = userDetailsService.loadUserByUsername(userId)
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
        filterChain.doFilter(request, response)
    }
}