package com.min.rsvp.repository

import com.min.rsvp.domain.RSVP
import com.min.rsvp.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RSVPRepository : JpaRepository<RSVP, Long> {
    fun findByUserAndIsActiveIs(user: User, isActive: Boolean, pageable: Pageable): List<RSVP>
    fun findByUserAndIsActiveIs(user: User, isActive: Boolean): RSVP?
}