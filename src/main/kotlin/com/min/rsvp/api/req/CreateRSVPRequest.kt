package com.min.rsvp.api.req

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.Instant

//todo double check email validation
data class CreateRSVPRequest (
    @field:NotBlank
    val name: String,
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val startOn: Instant,
    @field:NotBlank
    val endOn: Instant,
    @field:NotBlank
    val location: String,
    @field:NotEmpty
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?
)