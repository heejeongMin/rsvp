package com.min.rsvp.domain

import com.min.rsvp.api.req.LoginRequest
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
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
    var lastAccessedOn: Instant
) {

    fun updateAccessTime() {
      this.lastAccessedOn = Instant.now()
    }

    companion object {
        fun create(loginRequest: LoginRequest): User {
            return User(
                name = loginRequest.nickName,
                email = "",//currently not being able to retrieve email
                sns = loginRequest.issuer,
                snsId = loginRequest.id,
                isActive = true,
                createdOn = Instant.now(),
                lastAccessedOn = Instant.now()
            )
        }
    }
}