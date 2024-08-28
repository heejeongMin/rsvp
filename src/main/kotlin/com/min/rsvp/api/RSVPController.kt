package com.min.rsvp.api

import com.min.rsvp.api.req.CreateRSVPRequest
import com.min.rsvp.api.res.CreateRSVPResponse
import com.min.rsvp.api.res.GetRSVPActiveResponse
import com.min.rsvp.api.res.GetRSVPHistoryResponse
import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.service.RSVPService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController("/rsvp")
class RSVPController(
    val rsvpService: RSVPService
) {

    @PostMapping
    fun create(createRSVPRequest: CreateRSVPRequest, user: Principal) = ResponseEntity.ok(
        CreateRSVPResponse.from(rsvpService.create(RSVPDto.create(createRSVPRequest, user.name)))
    )

    @GetMapping
    fun getHistory(@RequestParam page: Int = 0, @RequestParam size: Int = 10, user: Principal) = ResponseEntity.ok(
        GetRSVPHistoryResponse.from(rsvpService.getHistory(page, size, user.name))
    )

    @GetMapping
    fun getActive(user: Principal) = ResponseEntity.ok(
        GetRSVPActiveResponse.from(rsvpService.getActive(user.name))
    )

    @PutMapping("/path")
    fun close(@PathVariable path: String, user: Principal): ResponseEntity.BodyBuilder {
        rsvpService.close(path, user.name)
        return ResponseEntity.ok()
    }
}