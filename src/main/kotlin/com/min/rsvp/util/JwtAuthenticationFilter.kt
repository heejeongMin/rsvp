package com.min.rsvp.util

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.web.filter.GenericFilterBean

@Order(1)
class JwtAuthenticationFilter : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token = (request as HttpServletRequest).getHeader("Authorization").substring(7)
        JwtHelper.decodeToken(token)

        if(request.requestURI == "/user/logout") {
            JwtHelper.invalidateToken(token)
        }
        chain!!.doFilter(request, response)
    }
}