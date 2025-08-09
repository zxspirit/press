package com.newzhxu.press.security.service

import com.newzhxu.press.security.repo.Role
import com.newzhxu.press.security.repo.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * @author zheng2580369@gmail.com
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MyUserDetailService::class, BCryptPasswordEncoder::class)
class MyUserDetailServiceTest {
    @Autowired
    lateinit var myUserDetailService: MyUserDetailService

    @Test

    fun createUser() {
        val user = myUserDetailService.createUser(User().apply {
            name = "testUser"
            pass = "testPassword"
            email = "123@s.com"
            enabled = true
            pressGrantedAuthorities.addAll(
                listOf(
                    Role().apply { name = "ROLE_ADMIN" },
                    Role().apply { name = "ROLE_USER" })
            )
        })
        println(user)

    }

    @Test
    fun updateUser() {
        myUserDetailService.updateUser(User().apply {
            name = "test"
            pass = "newPassword"
            email = "2344@dd.com"
            enabled = true
            pressGrantedAuthorities.addAll(
                listOf(
                    Role().apply { name = "ROLE_ADMIN" },
                    Role().apply { name = "ROLE_USER" })
            )
        })
    }

    @Test
    fun deleteUser() {
        myUserDetailService.deleteUser("test")
    }

    @Test
    fun changePassword() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("test", "123")
        myUserDetailService.changePassword("test", "newPassword")
    }

    @Test
    fun userExists() {
        val exists = myUserDetailService.userExists("test")
        println("User exists: $exists")
    }
}