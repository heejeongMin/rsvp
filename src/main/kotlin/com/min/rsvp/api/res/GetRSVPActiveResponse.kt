package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto

data class GetRSVPActiveResponse(
    val isActive: Boolean = true,
    val rsvp: List<GetRSVPResponse> = emptyList()
) {
    companion object {
        fun from(dto: List<RSVPDto>): GetRSVPActiveResponse {
            return GetRSVPActiveResponse(rsvp = dto.map { GetRSVPResponse.from(it) })
        }
    }
}
