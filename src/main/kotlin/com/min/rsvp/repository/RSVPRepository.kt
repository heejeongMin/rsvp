package com.min.rsvp.repository

import com.min.rsvp.domain.RSVP
import com.min.rsvp.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RSVPRepository : JpaRepository<RSVP, Long> {
    fun findByUserAndIsActiveIs(user: User, isActive: Boolean, pageable: Pageable): Page<RSVP>
    fun findByUserAndIsActiveIs(user: User, isActive: Boolean): List<RSVP>
    fun findOneByUserAndLinkContainsAndIsActive(user: User, link: String, isActive: Boolean): RSVP?
    fun findOneByLinkContains(link: String): RSVP?

}