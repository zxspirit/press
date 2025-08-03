package com.newzhxu.press.dns.repo

import jakarta.persistence.*

/**
 * @author zheng2580369@gmail.com
 */
@Entity
@Table(name = "users_a")
data class User(
    @GeneratedValue(strategy = GenerationType.AUTO) @Id var id: Long? = null,
    var username: String? = null,
    var password: String? = null,
    var email: String = "",
)