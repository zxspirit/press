package com.newzhxu.press.config.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * @author zheng2580369@gmail.com
 */
@ConfigurationProperties(prefix = "press")
data class PressProperties(

    @NestedConfigurationProperty var keyPair: ES256KeyPair
) {

}

// ES256 key pair configuration
data class ES256KeyPair(
    var privateKey: String = "",
    var publicKey: String = ""
) {
    fun getKeyPair(): KeyPair {

        val factory = KeyFactory.getInstance("EC")
        val private = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        val pub = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
        val publicKey1 = factory.generatePublic(pub)
        val privateKey1 = factory.generatePrivate(private)
        return KeyPair(publicKey1, privateKey1)
    }
}