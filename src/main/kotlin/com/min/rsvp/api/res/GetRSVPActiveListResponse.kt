package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto

data class GetRSVPActiveListResponse(
    val isActive: Boolean = true,
    val rsvp: List<GetRSVPResponse> = emptyList()
) {
    companion object {
        fun from(dto: List<RSVPDto>): GetRSVPActiveListResponse {
            return GetRSVPActiveListResponse(rsvp = dto.map { GetRSVPResponse.from(it) })
        }
    }
}
