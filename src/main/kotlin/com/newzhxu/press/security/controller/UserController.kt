package com.newzhxu.press.security.controller

import com.newzhxu.press.entity.ResponseInfo
import com.newzhxu.press.entity.Result
import com.newzhxu.press.entity.success
import com.newzhxu.press.security.repo.Role
import com.newzhxu.press.security.repo.User
import com.newzhxu.press.security.service.MyUserDetailService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @author zheng2580369@gmail.com
 */
@RestController
@RequestMapping("/user")
class UserController(val userDetailService: MyUserDetailService) {
    @PostMapping
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

    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteUser(@PathVariable name: String): Result<Void> {
        userDetailService.deleteUser(name)
        return success<Void>().apply { this.errors.add(ResponseInfo(200, "删除成功")) }
    }

}

class UserDto(
    @field:NotBlank(message = "用户名不能为空") val name: String,
    @field:Email val email: String,
    @field:NotBlank val password: String,
    @field:NotNull val enabled: Boolean = true,
) {
}