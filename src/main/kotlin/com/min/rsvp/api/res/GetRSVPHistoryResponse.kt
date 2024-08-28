package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto


data class GetRSVPHistoryResponse(
    val responses: List<GetRSVPResponse>
) {

    companion object {
        fun from(rsvps: List<RSVPDto>): GetRSVPHistoryResponse {
            return GetRSVPHistoryResponse(GetRSVPResponse.from(rsvps))
        }
    }
}

