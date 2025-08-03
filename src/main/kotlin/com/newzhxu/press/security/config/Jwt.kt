package com.newzhxu.press.security.config

import com.newzhxu.press.config.common.PressProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.sql.Date

/**
 * @author zheng2580369@gmail.com
 */
@Component
class JwtConfig(pressProperties: PressProperties) {
    private val keyPair = pressProperties.keyPair.getKeyPair()
    fun genToken(userId: String): String {
        try {
            return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(
                    keyPair.private,
                    SignatureAlgorithm.ES256
                )
                .compact()
        } catch (e: Exception) {
            throw JwtException("Failed to generate JWT token", e)
        }
    }

    fun getUserId(token: String): String {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(keyPair.public)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (ex: Exception) {
            throw JwtException("Invalid JWT token", ex)
        }
    }
}

class JwtException : AuthenticationException {
    constructor(message: String, exception: Exception) : super(message, exception)

}

