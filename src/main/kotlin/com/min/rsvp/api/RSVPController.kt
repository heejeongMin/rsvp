package com.min.rsvp.api

import com.min.rsvp.api.req.CreateRSVPRequest
import com.min.rsvp.api.res.CreateRSVPResponse
import com.min.rsvp.api.res.GetRSVPActiveResponse
import com.min.rsvp.api.res.GetRSVPHistoryResponse
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
    fun getActive(principal: Principal) = ResponseEntity.ok(
        GetRSVPActiveResponse.from(rsvpService.getActive(principal.name))
    )

    @PutMapping("/{path}")
    fun close(@PathVariable path: String, principal: Principal): ResponseEntity<String> {
        rsvpService.close(path, principal.name)
        return ResponseEntity.ok("success")
    }

    companion object {
        val LOGGER = KotlinLogging.logger {  }
    }
}