package com.newzhxu.press.config.security

import com.newzhxu.press.config.ES256KeyPair
import com.newzhxu.press.config.PressProperties
import com.newzhxu.press.security.config.getToken
import com.newzhxu.press.security.config.getUserId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.KeyPairGenerator
import java.util.*
import kotlin.test.Ignore
import kotlin.test.assertEquals

/**
 * @author zheng2580369@gmail.com
 */
const val testPrivateKey =
    "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCDbVg9n8mNWBNHwGaLCRtXmDitY1NbhSgAXtAxAQ3rM0Q=="
const val testPublicKey =
    "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEgj3Gr3Ol36jRfg1nC9krG31c+jQI7Zf9rFbS9aZj/aWEw4RdOK9EaREW7DojF3ZALKhGBu7QoGenSNXoVK0OKA=="

class JwtConfigTest {
    val logger: Logger = LoggerFactory.getLogger(JwtConfigTest::class.java)

    @Ignore
    @Test
    @DisplayName("Generate EC key pair")
    fun genKeyPair() {
        val instance = KeyPairGenerator.getInstance("EC")
        instance.initialize(256)
        val keyPair = instance.generateKeyPair()
        val private = keyPair.private
        val publicKey = keyPair.public
        println("private: " + Base64.getEncoder().encodeToString(private.encoded))
        println("public: " + Base64.getEncoder().encodeToString(publicKey.encoded))
    }

    @Test
    @DisplayName("Generate JWT token and validate user ID")
    fun genTokenTest() {
        val pressProperties = PressProperties(
            keyPairConfig = ES256KeyPair(
                testPrivateKey,
                testPublicKey
            )
        )
        val token = getToken(userId = "test", privateKey = pressProperties.keyPairConfig.keyPair.private)
        assertNotNull(token)
        logger.info("token: $token")
        val userId = getUserId(token, pressProperties.keyPairConfig.keyPair.public)
        assertEquals("test", userId)

    }

}