package com.min.rsvp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.min.rsvp.domain.dto.UserToken
import com.min.rsvp.domain.exception.ServiceException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import java.nio.charset.StandardCharsets.UTF_8
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object JwtHelper {
    /**
     * Enum class representing JWT algorithms with their corresponding values.
     */
    enum class JwtAlgorithm(val value: String) {
        ALGORITHM_HS256("HS256"), ALGORITHM_HS384("HS384"), ALGORITHM_HS512("HS512")
    }

    /**
     * Enum class representing HMAC signature algorithms with their corresponding values.
     */
    private enum class SignatureAlgorithm(val value: String) {
        ALGORITHM_HS256("HmacSHA256"), ALGORITHM_HS384("HmacSHA384"), ALGORITHM_HS512("HmacSHA512")
    }

    // Secret key for JWT generation and verification
    private var SECRET_KEY: String = "YmEyZjU5NzAtODMyZS00NDg4LWI3ZWQtMTZjNDdjNjYyZDI2KEIWTN123CILREPWLdfe09KKe"
    // Default expiration time for JWT tokens (365 days)
    private const val EXPIRATION_TIME_MS = 31536000000 // 365 days - Customizable

    // Default JWT algorithm and signature algorithm
    private var jwtAlgorithm: String = JwtAlgorithm.ALGORITHM_HS512.value
    private var signatureAlgorithm: String = SignatureAlgorithm.ALGORITHM_HS512.value

    /**
     * Initializes the JWT Helper with the secret key and algorithm.
     *
     * @param key Secret key for JWT token generation and verification.
     * @param algorithm JWT algorithm to be used (default is ALGORITHM_HS512).
     */
    fun init(key: String, algorithm: JwtAlgorithm = JwtAlgorithm.ALGORITHM_HS512) {
        SECRET_KEY = key
        checkForSecretKey()
        jwtAlgorithm = algorithm.value
        signatureAlgorithm = when (algorithm) {
            JwtAlgorithm.ALGORITHM_HS256 -> SignatureAlgorithm.ALGORITHM_HS256.value
            JwtAlgorithm.ALGORITHM_HS384 -> SignatureAlgorithm.ALGORITHM_HS384.value
            JwtAlgorithm.ALGORITHM_HS512 -> SignatureAlgorithm.ALGORITHM_HS512.value
        }
    }

    /**
     * Generates a JWT payload with customizable parameters.
     *
     * @param subject Subject of the JWT token.
     * @param issuer Issuer of the token (default is "yLnk").
     * @param audience Audience for the token (default is "yLnk").
     * @param issuedAt Time at which the token was issued (default is 0, current time used if set to 0).
     * @param expireAt Time at which the token will expire (default is 0, 365 days expiration if set to 0).
     * @param additionalData Additional data to include in the payload.
     * @return Map representing the JWT payload.
     */
    fun generatePayload(
        username: String,
        subject: String = "rsvp",
        issuer: String = "rsvp",
        audience: String = "rsvp",
        issuedAt: Long = 0,
        expireAt: Long = 0,
        additionalData: Map<String, Any>? = null
    ): Map<String, Any> {
            val temp = HashMap<String, Any>()
            val dateTime = Instant.now()
            val currentTime = if (issuedAt == "0".toLong()) dateTime else issuedAt
            temp["iss"] = issuer
            temp["iat"] = currentTime
            temp["exp"] = if (expireAt == "0".toLong()) {
                dateTime.plusSeconds(86400)
            } else {
                expireAt.plus(86400)
            }
            temp["aud"] = audience
            temp["sub"] = subject
            temp["username"] = username
            additionalData?.let {
                temp["additional"] = it
            }

        return  temp
    }

    /**
     * Generates a JWT token using the provided payload.
     *
     * @param payload JWT payload to be encoded into the token.
     * @return Generated JWT token.
     */
    fun generateJwtToken(payload: Map<String, Any>): String {
        checkForSecretKey()
        val header = encodeBase64URL("{\"alg\":\"$jwtAlgorithm\",\"typ\":\"JWT\"}".toByteArray(UTF_8))
        val encodedPayload = encodeBase64URL(serializeToJson(payload).toByteArray(UTF_8))
        val signature = generateSignature("$header.$encodedPayload")
        return "$header.$encodedPayload.$signature"
    }

    // 토큰 유효성, 만료일자 확인
    fun validateToken(jwtToken: String?): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: java.lang.Exception) {
            false
        }
    }

    fun decodeSNSToken(token: String) {
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            throw Exception("token not valid")
        }

        val payload = ObjectMapper().readValue(
            String(Base64.getUrlDecoder().decode(parts[1]), UTF_8), Map::class.java)?: throw Exception("token not valid")

        if(Instant.now().plusMillis(1000).isAfter(Instant.ofEpochSecond(payload["exp"].toString().toLong()))) {
            throw Exception("token has expired")
        }
    }

    fun decodeToken(token: String) {
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            throw Exception("token not valid")
        }

        val payload = ObjectMapper().readValue(
            String(Base64.getUrlDecoder().decode(parts[1]), UTF_8), Map::class.java)?: throw Exception("token not valid")

        if(Instant.now().plusMillis(1000).isAfter(Instant.parse(payload["exp"].toString()))) {
            throw Exception("token has expired")
        }
    }


    /**
     * Verifies the validity of a JWT token.
     *
     * @param token JWT token to be verified.
     * @return True if the token is valid, false otherwise.
     */
    fun verifyJwtToken(token: String): Boolean {
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            return false
        }
        val header = parts[0]
        val payload = parts[1]
        val signature = parts[2]
        val calculatedSignature = generateSignature("$header.$payload")
        return calculatedSignature == signature
    }

    /**
     * Refreshes a JWT token by updating its issued at and expiration time.
     *
     * @param token JWT token to be refreshed.
     * @return Refreshed JWT token.
     * @throws IllegalArgumentException if the token format is invalid.
     */
    fun refreshJwtToken(token: String): String {
        checkForSecretKey()
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid JWT format")
        }
        val header = parts[0]
        val payload = parts[1]
        val payloadJson = decodeBase64URL(payload)
        val payloadMap = ObjectMapper().readValue(payloadJson, Map::class.java) as MutableMap<String, Any>

        val currentTime = System.currentTimeMillis()
        payloadMap["iat"] = currentTime
        payloadMap["exp"] = currentTime + EXPIRATION_TIME_MS
        val newPayload = encodeBase64URL(serializeToJson(payloadMap).toByteArray(UTF_8))

        val newSignature = generateSignature("$header.$newPayload")
        return "$header.$newPayload.$newSignature"
    }

    fun invalidateToken(token: String): String {
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid JWT format")
        }
        val header = parts[0]
        val payload = parts[1]
        val payloadJson = decodeBase64URL(payload)
        val payloadMap = ObjectMapper().readValue(payloadJson, Map::class.java) as MutableMap<String, Any>

        val currentTime = System.currentTimeMillis()
        payloadMap["exp"] = currentTime.minus(9999999)
        val newPayload = encodeBase64URL(serializeToJson(payloadMap).toByteArray(UTF_8))

        val newSignature = generateSignature("$header.$newPayload")
        return "$header.$newPayload.$newSignature"
    }


    /**
     * Checks if a JWT token has expired.
     *
     * @param token JWT token to be checked.
     * @return True if the token has expired, false otherwise.
     */
    fun isTokenExpired(token: String): Boolean {
        checkForSecretKey()
        val payloadMap = extractPayload(token) ?: return true
        val expiration = payloadMap["exp"] as? Long ?: return true
        return System.currentTimeMillis() > expiration
    }

    /**
     * Decodes a base64 URL-encoded string.
     *
     * @param input Base64 URL-encoded string to decode.
     * @return Decoded string.
     */
    private fun decodeBase64URL(input: String): String {
        val decodedBytes = Base64.getUrlDecoder().decode(input)
        return String(decodedBytes, UTF_8)
    }

    /**
     * Extracts and decodes the payload from a JWT token.
     *
     * @param token JWT token from which to extract the payload.
     * @return Decoded payload as a Map or null if the token format is invalid.
     */
    fun extractPayload(token: String): Map<String, Any>? {
//        checkForSecretKey()
        val parts = token.split("\\.".toRegex())
        if (parts.size != 3) {
            return null
        }
        val payloadBase64 = parts[1]
        val payloadJson = String(Base64.getUrlDecoder().decode(payloadBase64), UTF_8)
        return ObjectMapper().readValue(payloadJson, Map::class.java) as? Map<String, Any> ?: null
    }

    /**
     * Generates the signature for a given data using the specified signature algorithm.
     *
     * @param data Data for which the signature is generated.
     * @return Base64 URL-encoded signature.
     */
    private fun generateSignature(
        data: String,
    ): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), signatureAlgorithm)
        val mac = Mac.getInstance(signatureAlgorithm)
        mac.init(secretKeySpec)
        val signatureBytes = mac.doFinal(data.toByteArray())
        return encodeBase64URL(signatureBytes)
    }

    /**
     * Encodes a byte array into a base64 URL-encoded string and removes padding.
     *
     * @param input Byte array to encode.
     * @return Base64 URL-encoded string without padding.
     */
    private fun encodeBase64URL(input: ByteArray): String {
        val encoded = Base64.getUrlEncoder().encodeToString(input)
        return encoded.replace("=", "")
    }

    /**
     * Serializes a Map of key-value pairs into a JSON-formatted string.
     *
     * @param data Map of key-value pairs to be serialized.
     * @return JSON-formatted string representing the serialized data.
     */
    private fun serializeToJson(data: Map<String, Any>): String {
        val entries = data.entries.joinToString(",") { "\"${it.key}\":\"${it.value}\"" }
        return "{$entries}"
    }

    /**
     * Checks if the secret key is empty and throws an exception if it is.
     *
     * @throws InvalidJWTSecretKey if the secret key is empty.
     */
    private fun checkForSecretKey() {
        if (SECRET_KEY.isEmpty()) throw Exception("secret key is empty")
    }

    fun parse(token: String?): UserToken {
        val claims: Claims =  Jwts.parser()
            .setSigningKey(SECRET_KEY.toByteArray())
            .parseClaimsJws(token)
            .body

        return UserToken.of(claims["username", String::class.java], claims.expiration.time)
    }
}
