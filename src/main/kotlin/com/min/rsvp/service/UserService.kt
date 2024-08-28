package com.min.rsvp.service

import com.min.rsvp.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {

    fun getUser(username: String) = userRepository.findByName(username)
}