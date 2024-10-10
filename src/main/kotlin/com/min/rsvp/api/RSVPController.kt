package com.min.rsvp.api

import com.min.rsvp.api.req.CreateRSVPRequest
import com.min.rsvp.api.req.GuestRSVPRequest
import com.min.rsvp.api.res.*
import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.service.RSVPService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/rsvp")
class RSVPController(
    val rsvpService: RSVPService
) {

    @PostMapping
    fun create(
        @RequestBody createRSVPRequest: CreateRSVPRequest,
        principal: Principal
    ) : ResponseEntity<CreateRSVPResponse> {
        LOGGER.info { "received $createRSVPRequest" }
       return  ResponseEntity.ok(
            CreateRSVPResponse.from(rsvpService.create(RSVPDto.create(createRSVPRequest, principal.name)))
        )
    }

    @GetMapping("/history")
    fun getHistory(@RequestParam page: Int = 0, @RequestParam size: Int = 10, principal: Principal) = ResponseEntity.ok(
        GetRSVPHistoryResponse.from(rsvpService.getHistory(page, size, principal.name))
    )

    @GetMapping
    fun getActiveRSVPList(principal: Principal) = ResponseEntity.ok(
        GetRSVPActiveListResponse.from(rsvpService.getActiveRSVPList(principal.name))
    )

    @GetMapping("/form/{path}")
    fun getActiveRSVP(@PathVariable path: String) = ResponseEntity.ok(
        GetRSVPResponse.from(rsvpService.getActiveRSVP(path))
    )

    @DeleteMapping("/{path}")
    fun close(@PathVariable path: String, principal: Principal): ResponseEntity<String> {
        rsvpService.close(path, principal.name)
        return ResponseEntity.ok("success")
    }

    @PostMapping("/response")
    fun response(@RequestBody guestRSVPRequest: GuestRSVPRequest): ResponseEntity<String> {
        rsvpService.rsvp(guestRSVPRequest)
        return ResponseEntity.ok("success")
    }

    companion object {
        val LOGGER = KotlinLogging.logger {  }
    }
}