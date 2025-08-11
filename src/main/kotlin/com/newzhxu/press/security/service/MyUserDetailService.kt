package com.newzhxu.press.security.service

import com.newzhxu.press.security.repo.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * @author zheng2580369@gmail.com
 */
@Component
class MyUserDetailService(
    val userRepo: UserRepo,
    val roleRepo: RoleRepo,
    val userRoleRelationRepo: UserRoleRelationRepo,
    val permissionRepo: PermissionRepo,
    val rolePermissionRelationRepo: RolePermissionRelationRepo,
    val passwordEncoder: PasswordEncoder,
) : UserDetailsManager, UserDetailsPasswordService {
    val logger: Logger = LoggerFactory.getLogger(MyUserDetailService::class.java)
    private var securityContextHolderStrategy: SecurityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy()

    @Transactional
    override fun createUser(user: UserDetails?) {
        if (user == null) {
            logger.error("User cannot be null")
            throw IllegalArgumentException("User cannot be null")
        }
        val details = user as User
        findUserInternal(details.username)?.run {
            throw IllegalArgumentException("User already exists")
        }
        details.pass = passwordEncoder.encode(user.pass)

        userRepo.save(details)
        userRoleRelationRepo.deleteUserRoleRelationsByUserName(details.name)
        details.authorities.forEach {
            findRoleInternal(it.authority) ?: run {
                throw IllegalArgumentException("Role does not exist: ${it.authority}")
            }
            userRoleRelationRepo.save(
                UserRoleRelation()
                    .apply {
                        userName = details.username
                        roleName = it.authority
                    }
            )
        }
    }

    @Transactional
    override fun updateUser(user: UserDetails?) {
        val user1 = user as User
        findUserInternal(user1.username) ?: run {
            throw IllegalArgumentException("User does not exist")
        }

        if (user1.pass.isNullOrBlank()) {
            logger.error("Password cannot be null or empty for user: ${user1.name}")
            throw IllegalArgumentException("Password cannot be null or empty")
        }
        user1.pass = passwordEncoder.encode(user1.pass)
        userRepo.save(user1)
        // Clear existing roles
        userRoleRelationRepo.deleteUserRoleRelationsByUserName(user1.name)
        // Add new roles
        user1.authorities.forEach {
            val existsById = roleRepo.existsById(it.authority)
            if (!existsById) {
                logger.error("Authority does not exist: ${it.authority}")
                throw IllegalArgumentException("Authority does not exist: ${it.authority}")
            }
            userRoleRelationRepo.save(
                UserRoleRelation()
                    .apply {
                        userName = user1.username
                        roleName = it.authority
                    }
            )
        }
    }

    @Transactional
    override fun deleteUser(username: String?) {
        findUserInternal(username) ?: run {
            throw IllegalArgumentException("Cannot delete user, user is not found")
        }

        userRepo.deleteById(username!!)
        userRoleRelationRepo.deleteUserRoleRelationsByUserName(username)
    }

    /**
     * 两个密码都是明文密码
     * 只能在用户登录状态下修改密码
     */
    @Transactional
    override fun changePassword(oldPassword: String?, newPassword: String?) {
        val authentication = securityContextHolderStrategy.context.authentication
        val user = findUserInternal(authentication.name) ?: run {
            throw IllegalArgumentException("User does not exist")
        }

        val oldEncode = passwordEncoder.encode(oldPassword)
        if (passwordEncoder.matches(user.pass, oldEncode)) {
            logger.error("Old password does not match for user: ${authentication.name}")
            throw IllegalArgumentException("Old password is incorrect")
        }
        user.pass = passwordEncoder.encode(newPassword)
        userRepo.save(user)
        logger.info("Password changed successfully for user: ${authentication.name}")
        // Optionally, you can clear the authentication context to force re-authentication
        securityContextHolderStrategy.clearContext()


    }

    @Transactional(readOnly = true)
    override fun userExists(username: String?): Boolean {
        val user = findUserInternal(username)
        return user != null
    }


    /**
     * newPassword 加密后的密码
     */
    @Transactional
    override fun updatePassword(
        user: UserDetails?,
        newPassword: String?
    ): UserDetails? {
        val entity = user as User
        findUserInternal(entity.name) ?: run {
            throw IllegalArgumentException("User does not exist")
        }
        if (newPassword.isNullOrBlank()) {
            throw IllegalArgumentException("New password cannot be null")
        }
        if (entity.pass == newPassword) {
            if (passwordEncoder.matches(newPassword, user.pass)) {
                logger.warn("New password is the same as the old password for user: ${entity.username}")
                throw IllegalArgumentException("New password cannot be the same as the old password")
            }
            entity.pass = newPassword
            userRepo.save(entity)
            logger.info("Password updated for user: ${entity.username}")
            return entity

        }
        return userRepo.findById(entity.username).orElse(null)
    }

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String?): UserDetails? {
        val user = findUserInternal(username) ?: throw UsernameNotFoundException("User not found: $username")
        userRoleRelationRepo.findUserRoleRelationsByUserName(user.name)
            .map { userRoleRelation ->
                val role = roleRepo.findById(userRoleRelation.roleName)
                    .orElseThrow { IllegalArgumentException("找不到角色名，请检查数据库角色 $userRoleRelation.roleName") }
                val grantedAuthorities = user.authorities as MutableSet<GrantedAuthority>
                grantedAuthorities.add(role)
                role
            }
            .forEach { role ->
                rolePermissionRelationRepo.findRolePermissionRelationsByRoleName(role.name)
                    .forEach { rolePermissionRelation ->
                        val permission = permissionRepo.findById(rolePermissionRelation.permissionName)
                            .orElseThrow { IllegalArgumentException("根据权限名查不到权限信息，检查数据完整性 ${rolePermissionRelation.permissionName}") }
                        val grantedAuthorities = user.authorities as MutableSet<GrantedAuthority>
                        grantedAuthorities.add(permission)
                    }

            }
        return user


    }

    private fun findUserInternal(username: String?): User? {
        if (username.isNullOrEmpty()) {
            logger.error("Username cannot be null or empty")
            throw IllegalArgumentException("Username cannot be null or empty")
        }
        return userRepo.findUserByName(username)
    }

    private fun findRoleInternal(roleName: String?): Role? {
        if (roleName.isNullOrEmpty()) {
            logger.error("Role name cannot be null or empty")
            throw IllegalArgumentException("Role name cannot be null or empty")
        }
        return roleRepo.findRoleByName(roleName)
    }
}



