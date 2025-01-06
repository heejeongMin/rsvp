package com.min.rsvp.api.req

data class GuestRSVPRequest(
    val path: String,
    val options: String,
    val name: String,
    val message: String? = ""
)