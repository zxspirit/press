package com.newzhxu.press.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @author zheng2580369@gmail.com
 */
class LoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.info(
            "Request Method: ${request.method}, Request URI: ${request.requestURI}, Remote Address: ${request.remoteAddr}, Request Headers: ${
                request.headerNames.toList().joinToString(", ") { "${it}: ${request.getHeader(it)}" }
            }"
        )
        filterChain.doFilter(request, response)
        logger.info("Response Status: ${response.status}, Content Type: ${response.contentType}")
    }
}