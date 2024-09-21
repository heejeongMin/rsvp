package com.min.rsvp.repository

import com.min.rsvp.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByName(name: String) : User?
    fun findBySnsId(snsId: String) : User?
}