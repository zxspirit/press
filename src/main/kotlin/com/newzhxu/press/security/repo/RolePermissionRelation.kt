package com.newzhxu.press.security.repo

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author zheng2580369@gmail.com
 */
@IdClass(RolePermissionRelationIndex::class)
@Entity
@Table(name = "role_permission_relations")
class RolePermissionRelation {
    @Id
    var roleName: String = ""

    @Id
    var permissionName: String = ""

    constructor() {}

    constructor(roleName: String, permissionName: String) {
        this.roleName = roleName
        this.permissionName = permissionName
    }

    override fun toString(): String {
        return "RolePermissionRelation(roleName='$roleName', permissionName='$permissionName')"
    }
}

class RolePermissionRelationIndex {
    var roleName: String = ""
    var permissionName: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RolePermissionRelationIndex) return false
        return roleName == other.roleName && permissionName == other.permissionName
    }

    override fun hashCode(): Int {
        var result = roleName.hashCode()
        result = 31 * result + permissionName.hashCode()
        return result
    }
}

interface RolePermissionRelationRepo : JpaRepository<RolePermissionRelation, RolePermissionRelationIndex> {
    fun findRolePermissionRelationsByRoleName(roleName: String): MutableList<RolePermissionRelation>

}