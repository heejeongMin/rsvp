package com.min.rsvp.service

import com.min.rsvp.api.req.LoginRequest
import com.min.rsvp.domain.User
import com.min.rsvp.repository.UserRepository
import com.min.rsvp.util.JwtHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
) {

    fun getUser(username: String) = userRepository.findByName(username)

    @Transactional
    fun login(req: LoginRequest): String {
        userRepository.findByName(req.nickName)?: userRepository.save(User.create(req))
        return JwtHelper.generateJwtToken(JwtHelper.generatePayload())
    }

    @Transactional(readOnly = false)
    fun logout(snsId: String) {
        val user = userRepository.findBySnsId(snsId)?: throw Error("user not found")
        user.updateAccessTime()
    }
}