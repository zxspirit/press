package com.newzhxu.press.security.repo

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

/**
 * @author zheng2580369@gmail.com
 */
@Entity
@Table(name = "user_role_relations")
@IdClass(UserRoleRelationIndex::class)
class UserRoleRelation {

    @Id
    @Column(name = "user_name")
    var userName: String = ""

    @Id
    @Column(name = "role_name")
    var roleName: String = ""


}

class UserRoleRelationIndex : Serializable {
    var userName: String = ""
    var roleName: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserRoleRelationIndex) return false
        return userName == other.userName && roleName == other.roleName
    }

    override fun hashCode(): Int {
        var result = userName.hashCode()
        result = 31 * result + roleName.hashCode()
        return result
    }


}

interface UserRoleRelationRepo : JpaRepository<UserRoleRelation, UserRoleRelationIndex> {
    fun findUserRoleRelationsByUserName(userName: String): MutableList<UserRoleRelation>
    fun deleteUserRoleRelationsByUserName(userName: String)
}