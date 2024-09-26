package com.min.rsvp.api.res

import com.min.rsvp.domain.RSVP
import org.springframework.data.domain.Page

data class PageInfo(
    val numberOfElements: Int,
    val pageNumber: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLast: Boolean
) {
    companion object {
        fun from(page: Page<RSVP>): PageInfo {
            return PageInfo(
                numberOfElements = page.numberOfElements,
                pageNumber = page.number,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                isLast = page.isLast
            )
        }
    }
}