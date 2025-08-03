package com.newzhxu.press.security.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

/**
 * @author zheng2580369@gmail.com
 */
@Component
class UserDetailConfig() : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return User(username, "", listOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_USER")))
    }

}

