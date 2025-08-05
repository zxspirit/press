package com.newzhxu.press.security.service

import com.newzhxu.press.security.repo.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * @author zheng2580369@gmail.com
 */
@Component
class JwtUserDetailService(
    val userRepo: UserRepo,
    val roleRepo: RoleRepo,
    val userRoleRelationRepo: UserRoleRelationRepo,
    val permissionRepo: PermissionRepo,
    val rolePermissionRelationRepo: RolePermissionRelationRepo,
) : UserDetailsManager, UserDetailsPasswordService {
    val logger: Logger = LoggerFactory.getLogger(JwtUserDetailService::class.java)
    private var securityContextHolderStrategy: SecurityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy()

    @Transactional
    override fun createUser(user: UserDetails?) {
        if (user == null) {
            logger.error("User cannot be null")
            throw IllegalArgumentException("User cannot be null")
        }
        val details = user as User
        if (details.name.isEmpty()) {
            logger.error("Username cannot be null or empty")
            throw IllegalArgumentException("Username cannot be null or empty")
        }
        val findUserByName = userRepo.findUserByName(user.name)
        if (findUserByName != null) {
            logger.error("User already exists: ${details.name}")
            throw IllegalArgumentException("User already exists")
        }
        userRepo.save(details)

        details.authorities.forEach {
            val existsById = roleRepo.existsById(it.authority)
            if (!existsById) {
                logger.error("Authority does not exist: ${it.authority}")
                throw IllegalArgumentException("Authority does not exist: ${it.authority}")
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
        val exist = userRepo.existsById(user1.name)
        if (!exist) {
            logger.error("User does not exist: ${user1.name}")
            throw IllegalArgumentException("User does not exist")
        }
        if (user1.pass.isNullOrBlank()) {
            logger.error("Password cannot be null or empty for user: ${user1.name}")
            throw IllegalArgumentException("Password cannot be null or empty")
        }
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
        if (username.isNullOrEmpty()) {
            logger.error("Username cannot be null or empty")
            throw IllegalArgumentException("Username cannot be null or empty")
        }
        val exist = userRepo.existsById(username)
        if (!exist) {
            logger.error("User does not exist: $username")
            throw IllegalArgumentException("User does not exist")
        }

        userRepo.deleteById(username)
        userRoleRelationRepo.deleteUserRoleRelationsByUserName(username)
    }

    @Transactional
    override fun changePassword(oldPassword: String?, newPassword: String?) {
        val authentication = securityContextHolderStrategy.context.authentication
        val user = userRepo.findById(authentication.name).orElse(null)
        if (user == null) {
            logger.error("User not found: ${authentication.name}")
            throw IllegalArgumentException("User not found")
        }
        if (user.pass != oldPassword) {
            logger.error("Old password does not match for user: ${authentication.name}")
            throw IllegalArgumentException("Old password is incorrect")
        }
        user.pass = newPassword
        userRepo.save(user)
        logger.info("Password changed successfully for user: ${authentication.name}")
        // Optionally, you can clear the authentication context to force re-authentication
        securityContextHolderStrategy.clearContext()


    }

    @Transactional(readOnly = true)
    override fun userExists(username: String?): Boolean {
        if (username.isNullOrEmpty()) {
            logger.error("Username cannot be null or empty")
            throw IllegalArgumentException("Username cannot be null or empty")
        }
        return userRepo.existsById(username)
    }

    @Transactional
    override fun updatePassword(
        user: UserDetails?,
        newPassword: String?
    ): UserDetails? {
        val entity = user as User
        if (newPassword.isNullOrBlank()) {
            throw IllegalArgumentException("New password cannot be null")
        }
        val exist = userRepo.existsById(entity.username)
        if (!exist) {
            logger.error("User does not exist: ${entity.username}")
            throw IllegalArgumentException("User does not exist")
        }
        if (entity.pass == newPassword) {
            logger.warn("New password is the same as the old password for user: ${entity.username}")
            throw IllegalArgumentException("New password cannot be the same as the old password")
        }
        entity.pass = newPassword
        userRepo.save(entity)
        logger.info("Password updated for user: ${entity.username}")
        return entity

    }

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null) {
            logger.error("Username cannot be null")
            throw IllegalArgumentException("Username cannot be null")
        }
        val user = userRepo.findUserByName(username) ?: throw IllegalArgumentException("User not found")
        userRoleRelationRepo.findUserRoleRelationsByUserName(user.name)
            .map {
                val role = roleRepo.findById(it.roleName).orElseThrow()
                val roles = user.authorities as MutableList<GrantedAuthority>
                roles.add(role)
                role
            }
            .flatMap {
                val rolePermissionRelations =
                    rolePermissionRelationRepo.findRolePermissionRelationsByRoleName(it.name)
                rolePermissionRelations
            }
            .forEach {
                val permission = permissionRepo.findById(it.permissionName).orElseThrow()
                val roles = user.authorities as MutableList<GrantedAuthority>
                roles.add(permission)
            }
        return user
    }
}