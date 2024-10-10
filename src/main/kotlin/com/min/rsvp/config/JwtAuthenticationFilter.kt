package com.min.rsvp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.min.rsvp.domain.exception.ResultCode
import com.min.rsvp.domain.exception.ServiceException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

private const val HEADER_AUTHORIZATION = "Authorization"
private const val TOKEN_PREFIX = "Bearer "
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.requestURI == "/user/login" || request.requestURI == "/user/logout") {
            return snsLoginLogout(request, response, filterChain)
        }

        if (!request.requestURI.contains("/rsvp/form") && !request.requestURI.contains("/rsvp/response")) {
            try {
                val userToken = Optional.ofNullable(request.getHeader(HEADER_AUTHORIZATION))
                    .filter { it.startsWith(TOKEN_PREFIX) }
                    .map { it.substring(TOKEN_PREFIX.length) }
                    .map(JwtHelper::parse)
                    .filter { !it!!.isExpired() }
                    .orElse(null)

                if (userToken != null && SecurityContextHolder.getContext().authentication == null) {
                    val userDetails: UserDetails? = userDetailsService.loadUserByUsername(userToken.username)
                    if (userDetails != null) {
                        val authenticationToken =
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        SecurityContextHolder.getContext().authentication = authenticationToken
                    }
                }

                filterChain.doFilter(request, response)

            } catch (e: Exception) {
                val serviceEx = ServiceException(HttpStatus.UNAUTHORIZED, ResultCode.TOKEN_EXPIRED, e.message?:"")
                response.status = serviceEx.code.value()
                response.writer.write(ObjectMapper().writeValueAsString(serviceEx))
            }
        } else {
            filterChain.doFilter(request, response)
        }

    }

    private fun snsLoginLogout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization").substring(7)

        if (request.requestURI == "/user/login") {
            JwtHelper.decodeSNSToken(token)
        }

        if (request.requestURI == "/user/logout") {
            JwtHelper.invalidateToken(token)
        }

        filterChain.doFilter(request, response)
    }
}