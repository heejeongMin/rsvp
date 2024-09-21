package com.min.rsvp.api.req

data class LoginRequest(
    val id: String,
    val nickName: String,
    val issuer: String
)