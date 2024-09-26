package com.min.rsvp.domain.dto

import java.time.Instant

data class UserToken(
    val username: String,
    val exp: Long
) {
    fun isExpired(): Boolean {
        return Instant.now().plusMillis(1000).isAfter(Instant.ofEpochSecond(exp))
    }

    companion object {
        fun of(username: String, exp: Long): UserToken = UserToken(username, exp)
    }
}