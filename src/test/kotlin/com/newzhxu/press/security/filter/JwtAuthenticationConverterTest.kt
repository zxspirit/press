package com.newzhxu.press.security.filter

import com.newzhxu.press.config.security.testPublicKey
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * @author zheng2580369@gmail.com
 */
class JwtAuthenticationConverterTest {
    @Test
    fun convert() {
        val factory = KeyFactory.getInstance("EC")

        val pub = X509EncodedKeySpec(Base64.getDecoder().decode(testPublicKey))

        val converter = JwtAuthenticationConverter(factory.generatePublic(pub))
        val request = MockHttpServletRequest()
        request.addHeader(
            HttpHeaders.AUTHORIZATION,
            "$AUTHENTICATION_SCHEME_BEARER eyJhbGciOiJFUzI1NiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3NTQzNjM5OTEsImV4cCI6MTc1NDM2NzU5MX0.c5saagtMhsXjVW5SFkkx2fAwRjGRaGd7tCld9aQiRPKCyBKtSsOkxMxcbuLT8rypnHmWg9Cg_n6sR-tl5xSQ4Q"
        )
        val convert = converter.convert(request)
        assert(convert != null)
    }

}