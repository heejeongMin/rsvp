package com.min.rsvp.domain.exception

import org.springframework.http.HttpStatusCode
import java.lang.RuntimeException

data class ServiceException (
    val code: HttpStatusCode,
    val resultCode: ResultCode,
    override val message: String,
) : RuntimeException(message)