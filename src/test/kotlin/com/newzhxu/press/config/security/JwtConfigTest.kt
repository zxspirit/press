package com.newzhxu.press.config.security

import com.newzhxu.press.config.common.ES256KeyPair
import com.newzhxu.press.config.common.PressProperties
import com.newzhxu.press.security.config.JwtConfig
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
class JwtConfigTest {
    val logger: Logger = LoggerFactory.getLogger(JwtConfigTest::class.java)

    @Ignore
    @Test
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
    fun genToken() {
        val jwtConfig = JwtConfig(
            PressProperties(
                keyPair = ES256KeyPair(
                    "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCDbVg9n8mNWBNHwGaLCRtXmDitY1NbhSgAXtAxAQ3rM0Q==",
                    "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEgj3Gr3Ol36jRfg1nC9krG31c+jQI7Zf9rFbS9aZj/aWEw4RdOK9EaREW7DojF3ZALKhGBu7QoGenSNXoVK0OKA=="
                )
            )
        )
        val token = jwtConfig.genToken("123")
        assertNotNull(token)
        logger.info("token: $token")
        val userId = jwtConfig.getUserId(token)
        assertEquals("123", userId)

    }

}