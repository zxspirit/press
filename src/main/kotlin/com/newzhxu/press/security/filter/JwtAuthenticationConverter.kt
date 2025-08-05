package com.newzhxu.press.security.filter

import com.newzhxu.press.security.config.getUserId
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.web.authentication.AuthenticationConverter
import java.security.PublicKey

/**
 * @author zheng2580369@gmail.com
 */
const val AUTHENTICATION_SCHEME_BEARER: String = "Bearer"

class JwtAuthenticationConverter(val publicKey: PublicKey) : AuthenticationConverter {

    override fun convert(request: HttpServletRequest): JwtAuthenticationToken? {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        header = header.trim()
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)) {
            return null
        }
        if (header.equals(AUTHENTICATION_SCHEME_BEARER, true)) {
            throw BadCredentialsException("Authentication header cannot be blank")
        }
        val token: String = header.substring(AUTHENTICATION_SCHEME_BEARER.length).trim { it <= ' ' }
        val userId = getUserId(token, publicKey)

        return JwtAuthenticationToken(userId, token)

    }
}