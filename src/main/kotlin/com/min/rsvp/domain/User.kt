package com.min.rsvp.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val email: String,
    val sns: String,
    val snsId: String,
    val isActive: Boolean,
    val createdOn: Instant,
    val lastAccessedOn: Instant
)