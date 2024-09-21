package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.domain.dto.ResponderDto
import java.time.Instant

data class GetRSVPResponse(
    val name: String,
    val link: String?,
    val email: String,
    val startOn: Instant,
    val endOn: Instant,
    val location: String,
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?,
    val responders: List<RespondersRes>
) {
    companion object {
        fun from(dto: RSVPDto): GetRSVPResponse {
            return GetRSVPResponse(
                name = dto.name,
                link = dto.link,
                email = dto.email,
                startOn = dto.startOn,
                endOn = dto.endOn,
                location = dto.location,
                options = dto.options,
                timeLimit = dto.timeLimit,
                description = dto.description,
                responders = dto.responders.map { RespondersRes.from(it) }
            )
        }

        fun from(dtos: List<RSVPDto>): List<GetRSVPResponse> {
            return dtos.map { this.from(it) }
        }
    }
}

data class RespondersRes(
    val name: String,
    val option: String,
    val message: String?
) {
    companion object {
        fun from(dto: ResponderDto): RespondersRes {
            return RespondersRes(
                name = dto.name,
                option = dto.option,
                message = dto.message
            )
        }
    }
}