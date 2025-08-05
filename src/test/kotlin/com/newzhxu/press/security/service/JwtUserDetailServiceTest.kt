package com.newzhxu.press.security.service

import com.newzhxu.press.security.repo.Role
import com.newzhxu.press.security.repo.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author zheng2580369@gmail.com
 */
@SpringBootTest
class JwtUserDetailServiceTest {
    @Autowired
    lateinit var jwtUserDetailService: JwtUserDetailService

    @Test
//    @Transactional
    fun createUser() {
        jwtUserDetailService.createUser(User().apply {
            name = "test"
            pass = "123"

            enabled = true
            email = "1@c.com"
            pressGrantedAuthorities.add(
                Role().apply {
                    name = "ROLE_USER"
                    description = "用户角色"
                }
            )
        })
    }

    @Test
    fun loadUserByUsernameTest() {
        val user = jwtUserDetailService.loadUserByUsername("test")
        println(user)
    }

}