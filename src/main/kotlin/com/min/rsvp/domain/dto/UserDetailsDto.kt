package com.min.rsvp.domain.dto

import com.min.rsvp.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsDto (
    private val name: String
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return name
    }

    companion object {
        fun from(user: User?) : UserDetailsDto? {
            if(user == null) {
                return null
            }
            return UserDetailsDto(name = user.name)
        }
    }
}