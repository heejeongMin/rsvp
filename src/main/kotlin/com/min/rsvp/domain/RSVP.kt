package com.min.rsvp.domain

import com.min.rsvp.domain.dto.RSVPDto
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "rsvps")
class RSVP (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    val user: User,
    val name: String,
    val link: String,
    val email: String,
    val startOn: Instant,
    val endOn: Instant,
    val location: Instant,
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?,
    var isActive: Boolean,
    @OneToMany(mappedBy = "rsvp")
    val responders: List<Responders> = emptyList()
) {

    fun close() {
        isActive = false
    }

    companion object {
        fun create(dto: RSVPDto, user: User, host: String, path: String) : RSVP {
            return RSVP(
                name = dto.name,
                user = user,
                link = host + "/" + path + "/" + UUID.randomUUID(),
                email = dto.email,
                startOn = dto.startOn,
                endOn = dto.endOn,
                location = dto.location,
                options = dto.options,
                timeLimit = dto.timeLimit,
                isActive = true,
                description = dto.description
            )
        }
    }
}