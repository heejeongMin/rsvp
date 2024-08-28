package com.min.rsvp.domain.dto

import com.min.rsvp.domain.Responders

data class ResponderDto(
    val name: String,
    val option: String,
    val message: String?,
) {
    companion object {
        fun from(responder: Responders): ResponderDto {
            return ResponderDto(
                name = responder.name,
                option = responder.option,
                message = responder.message
            )
        }

        fun from(responders: List<Responders>): List<ResponderDto> {
            return responders.map { this.from(it) }
        }
    }
}