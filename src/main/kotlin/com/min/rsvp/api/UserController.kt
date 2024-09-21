package com.min.rsvp.api

import com.min.rsvp.api.req.LoginRequest
import com.min.rsvp.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
) {
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): String {
       return userService.login(req)
    }

    @PostMapping("/logout")
    fun login(@RequestBody map: Map<String, String>) {
        userService.logout(map["snsId"]!!)
    }
}