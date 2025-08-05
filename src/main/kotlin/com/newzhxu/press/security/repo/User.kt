package com.newzhxu.press.security.repo

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

/**
 * @author zheng2580369@gmail.com
 */
@Entity
@Table(name = "users")
class User : UserDetails {

    @Id
    var name: String = ""
    var pass: String? = null
    var enabled: Boolean? = null
    var email: String? = null

    @Transient
    var pressGrantedAuthorities: MutableList<Role> = mutableListOf()


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return pressGrantedAuthorities
    }

    override fun getPassword(): String? {
        return pass
    }

    override fun getUsername(): String {
        return name
    }


}

@Repository
interface UserRepo : JpaRepository<User, String> {
    fun deleteUserByName(username: String?)
    fun findUserByName(name: String): User?
}