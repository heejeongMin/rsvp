package com.min.rsvp.api.res

import com.min.rsvp.domain.dto.RSVPDto


data class GetRSVPHistoryResponse(
    val responses: List<GetRSVPResponse>,
    val pageInfo: PageInfo
) {

    companion object {
        fun from(rsvps: Pair<List<RSVPDto>, PageInfo>): GetRSVPHistoryResponse {
            return GetRSVPHistoryResponse(GetRSVPResponse.from(rsvps.first), rsvps.second)
        }
    }
}

