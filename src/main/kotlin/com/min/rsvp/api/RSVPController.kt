package com.min.rsvp.api

import com.min.rsvp.api.req.CreateRSVPRequest
import com.min.rsvp.api.res.CreateRSVPResponse
import com.min.rsvp.api.res.GetRSVPActiveResponse
import com.min.rsvp.api.res.GetRSVPHistoryResponse
import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.service.RSVPService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rsvp")
class RSVPController(
    val rsvpService: RSVPService
) {

    @PostMapping
    fun create(@RequestBody createRSVPRequest: CreateRSVPRequest) = ResponseEntity.ok(
        CreateRSVPResponse.from(rsvpService.create(RSVPDto.create(createRSVPRequest, "heejeong")))
    )

    @GetMapping("/history")
    fun getHistory(@RequestParam page: Int = 0, @RequestParam size: Int = 10) = ResponseEntity.ok(
        GetRSVPHistoryResponse.from(rsvpService.getHistory(page, size, "heejeong"))
    )

    @GetMapping
    fun getActive() = ResponseEntity.ok(
        GetRSVPActiveResponse.from(rsvpService.getActive("heejeong"))
    )

    @PutMapping("/{path}")
    fun close(@PathVariable path: String): ResponseEntity<String> {
        rsvpService.close(path, "heejeong")
        return ResponseEntity.ok("success")
    }
}