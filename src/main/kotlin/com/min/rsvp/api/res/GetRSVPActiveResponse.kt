package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.domain.dto.ResponderDto
import java.time.Instant

data class GetRSVPActiveResponse(
    val isActive: Boolean = false,
    val rsvp: GetRSVPResponse? = null
) {
    companion object {
        fun from(dto: RSVPDto?): GetRSVPActiveResponse {
            if (dto == null) {
                return GetRSVPActiveResponse()
            }

            return GetRSVPActiveResponse(
                isActive = true,
                rsvp = GetRSVPResponse.from(dto)
            )
        }

    }
}
