package com.min.rsvp.domain.dto

import com.min.rsvp.api.req.CreateRSVPRequest
import com.min.rsvp.domain.RSVP
import java.time.Instant

data class RSVPDto(
    val name: String,
    val username: String,
    val link: String? = null,
    val email: String,
    val startOn: Instant,
    val endOn: Instant,
    val location: String,
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?,
    val responders: List<ResponderDto> = emptyList(),
    val isActive: Boolean
) {

    companion object {
        fun create(req: CreateRSVPRequest, username: String): RSVPDto {
            return RSVPDto(
                name = req.name,
                username = username,
                email = req.email,
                startOn = req.startOn,
                endOn = req.endOn,
                location = req.location,
                options = req.options,
                timeLimit = req.timeLimit,
                description = req.description,
                isActive = true
            )
        }

        fun from(rsvp: RSVP): RSVPDto {
            return RSVPDto(
                name = rsvp.name,
                username = rsvp.user.name,
                link = rsvp.link,
                email = rsvp.hostEmail,
                startOn = rsvp.startOn,
                endOn = rsvp.endOn,
                location = rsvp.location,
                options = rsvp.options,
                timeLimit = rsvp.timeLimit,
                description = rsvp.description,
                responders = ResponderDto.from(rsvp.responders),
                isActive = rsvp.isActive
            )
        }

        fun from(rsvps: List<RSVP>): List<RSVPDto> {
            return rsvps.map { this.from(it) }
        }
    }
}