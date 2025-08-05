package com.newzhxu.press.security.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.AuthenticationException
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*

/**
 * @author zheng2580369@gmail.com
 */


class JwtException : AuthenticationException {
    constructor(message: String, exception: Exception) : super(message, exception)

}

fun getToken(userId: String, time: Long = 60 * 60 * 1000, privateKey: PrivateKey): String {
    try {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + time))
            .signWith(
                privateKey,
                SignatureAlgorithm.ES256
            )
            .compact()
    } catch (e: Exception) {
        throw JwtException("Failed to generate JWT token", e)
    }
}

fun getUserId(token: String, publicKey: PublicKey): String {
    try {
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    } catch (ex: Exception) {
        throw JwtException("Invalid JWT token", ex)
    }
}

