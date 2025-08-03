package com.newzhxu.press.security.repo

import jakarta.persistence.*
import java.io.Serializable

/**
 * user_role
 * This class represents the relationship between users and roles in the security module.
 * @author zheng2580369@gmail.com
 */
@Entity
@Table(name = "user_role", indexes = [Index(name = "user_role_idx", columnList = "user_id, role_id")])
@IdClass(UserRoleId::class)
class UserRole {
    // Define fields for user ID and role ID
    @Id
    @Column(name = "user_id")
    var userId: Long? = null

    @Id
    @Column(name = "role_id")
    var roleId: Long? = null

    // Define any additional fields or methods as needed
    // For example, you might want to add a constructor, getters, setters, etc.
}

data class UserRoleId(
    val userId: Long,
    val roleId: Long
) : Serializable {

}