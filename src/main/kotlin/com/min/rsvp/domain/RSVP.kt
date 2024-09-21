package com.min.rsvp.domain

import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.util.RSVPOptionsConverter
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
    val hostEmail: String,
    val startOn: Instant,
    val endOn: Instant,
    val location: String,
    @Convert(converter = RSVPOptionsConverter::class)
    val options: List<String>,
    val timeLimit: Instant?,
    val description: String?,
    var isActive: Boolean,
    @OneToMany(mappedBy = "rsvp")
    val responders: List<Responders> = emptyList(),
    val createdOn: Instant,
    val updatedOn: Instant
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
                hostEmail = dto.email,
                startOn = dto.startOn,
                endOn = dto.endOn,
                location = dto.location,
                options = dto.options,
                timeLimit = dto.timeLimit,
                isActive = true,
                description = dto.description,
                createdOn = Instant.now(),
                updatedOn = Instant.now()
            )
        }
    }
}