package com.newzhxu.press.security.repo

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority

/**
 * authority entity
 * @author zheng2580369@gmail.com
 */
@Entity
@Table(name = "permissions")

class Permission : GrantedAuthority {
    @Id
    var name: String = ""
    var description: String? = null

    override fun getAuthority(): String? {
        return name
    }
}

interface PermissionRepo : JpaRepository<Permission, String> {
    fun findAuthorityByName(name: String): Permission?
    fun deleteAuthorityByName(name: String)
}