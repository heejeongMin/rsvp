package com.min.rsvp.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "responders")
class Responders (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @ManyToOne(optional = false)
    @JoinColumn(name = "rsvp_id")
    val rsvp: RSVP,
    val name: String,
    val option: String,
    val message: String?,
    val createdOn: Instant
)