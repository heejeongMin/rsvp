package com.min.rsvp.service

import com.min.rsvp.api.req.LoginRequest
import com.min.rsvp.domain.User
import com.min.rsvp.domain.dto.UserDetailsDto
import com.min.rsvp.repository.UserRepository
import com.min.rsvp.config.JwtHelper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
): UserDetailsService {

    fun getUser(username: String) = userRepository.findByName(username)

    @Transactional
    fun login(req: LoginRequest): String {
        userRepository.findByName(req.nickName)?: userRepository.save(User.create(req))
        return JwtHelper.generateJwtToken(JwtHelper.generatePayload(req.nickName))
    }

    @Transactional(readOnly = false)
    fun logout(snsId: String) {
        val user = userRepository.findBySnsId(snsId)?: throw Error("user not found")
        user.updateAccessTime()
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        val user = getUser(username)
       return UserDetailsDto.from(user)
    }
}