package com.newzhxu.press.security.controller

import com.newzhxu.press.common.ResponseInfo
import com.newzhxu.press.common.Result
import com.newzhxu.press.common.success
import com.newzhxu.press.security.repo.Role
import com.newzhxu.press.security.repo.User
import com.newzhxu.press.security.service.MyUserDetailService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author zheng2580369@gmail.com
 */
@RestController
@RequestMapping("/user")
class UserController(val userDetailService: MyUserDetailService) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody userDto: UserDto): Result<Void> {


        userDetailService.createUser(
            User()
                .apply {
                    this.email = userDto.email
                    this.name = userDto.name
                    this.pass = userDto.password
                    this.enabled = userDto.enabled
                    this.pressGrantedAuthorities.add(Role().apply { name = "ROLE_USER" })
                }
        )
        return success<Void>().apply { this.errors.add(ResponseInfo(200, "注册成功")) }
    }

}

class UserDto(
    @field:NotBlank(message = "用户名不能为空") val name: String,
    @field:Email val email: String,
    @field:NotBlank val password: String,
    @field:NotNull val enabled: Boolean = true,
) {
}