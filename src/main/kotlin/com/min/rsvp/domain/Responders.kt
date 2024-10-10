package com.min.rsvp.domain

import com.min.rsvp.api.req.GuestRSVPRequest
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "responders")
class Responders (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "rsvp_id")
    val rsvp: RSVP,
    val name: String,
    val option: String,
    val message: String?,
    val createdOn: Instant
) {
    companion object {
        fun create(guestRSVPRequest: GuestRSVPRequest, rsvp: RSVP) : Responders{
            return Responders(
                rsvp = rsvp,
                name = guestRSVPRequest.name,
                option = guestRSVPRequest.option,
                message = guestRSVPRequest.message,
                createdOn = Instant.now()
            )
        }
    }
}