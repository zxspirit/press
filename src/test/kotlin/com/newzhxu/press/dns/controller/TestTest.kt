package com.newzhxu.press.dns.controller

import com.newzhxu.press.config.common.PressProperties
import com.newzhxu.press.security.config.SecurityConfig
import com.newzhxu.press.security.filter.JwtFilter
import com.newzhxu.press.security.repo.*
import com.newzhxu.press.security.service.JwtUserDetailService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * @author zheng2580369@gmail.com
 */
@WebMvcTest(controllers = [TestController::class])
@Import(value = [SecurityConfig::class, JwtUserDetailService::class])
class TestTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockitoBean
    lateinit var jwtFilter: JwtFilter

    @MockitoBean
    lateinit var pressProperties: PressProperties

    @MockitoBean
    lateinit var userRepo: UserRepo

    @MockitoBean
    lateinit var roleRepo: RoleRepo

    @MockitoBean
    lateinit var userRoleRelationRepo: UserRoleRelationRepo

    @MockitoBean
    lateinit var permissionRepo: PermissionRepo

    @MockitoBean
    lateinit var rolePermissionRelationRepo: RolePermissionRelationRepo


    @Test
    @DisplayName("Test hello endpoint with JWT authentication")
    fun hello() {
        Mockito.doAnswer {
            val request = it.getArgument<ServletRequest>(0)
            val response = it.getArgument<ServletResponse>(1)
            val chain = it.getArgument<FilterChain>(2)
            // 这里模拟往SecurityContext放入一个用户
            val authentication = UsernamePasswordAuthenticationToken(
                "testUser",
                null,
                listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
            SecurityContextHolder.getContext().authentication = authentication
            chain.doFilter(request, response)
            null
        }.`when`(jwtFilter).doFilter(any(), any(), any())
        mvc.get("/dns/hello")
            .andExpect {
                status { isOk() }
                content { string("Hello, World!") }
            }
    }

}