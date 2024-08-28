package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto
import java.time.Instant

data class CreateRSVPResponse(
    val name: String,
    val link: String,
    val email: String,
    val startOn: Instant,
    val endOn: Instant,
    val location: Instant,
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?
) {

    companion object {
        fun from(dto: RSVPDto): CreateRSVPResponse {
            return CreateRSVPResponse(
                name = dto.name,
                link = dto.link!!,
                email = dto.email,
                startOn = dto.startOn,
                endOn = dto.endOn,
                location = dto.location,
                options = dto.options,
                timeLimit = dto.timeLimit,
                description = dto.description
            )
        }
    }
}