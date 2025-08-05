package com.newzhxu.press.security.filter

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * @author zheng2580369@gmail.com
 */
class JwtAuthenticationToken(
    val jwtPrincipal: Any?,
    val jwtCredentials: Any?,
) : AbstractAuthenticationToken(null) {
    val auths = mutableListOf<GrantedAuthority>()


    override fun getCredentials(): Any? {
        return jwtCredentials
    }

    override fun getPrincipal(): Any? {
        return jwtPrincipal
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return auths
    }
}