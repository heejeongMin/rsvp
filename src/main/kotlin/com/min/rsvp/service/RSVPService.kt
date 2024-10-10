package com.min.rsvp.service

import com.min.rsvp.api.req.GuestRSVPRequest
import com.min.rsvp.domain.RSVP
import com.min.rsvp.api.res.PageInfo
import com.min.rsvp.domain.exception.ResultCode
import com.min.rsvp.domain.Responders
import com.min.rsvp.domain.dto.RSVPDto
import com.min.rsvp.domain.exception.ServiceException
import com.min.rsvp.repository.RSVPRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

const val path = "rsvp"

@Service
class RSVPService(
    @Value("\${host}")
    val host: String,
    val userService: UserService,
    val rsvpRepository: RSVPRepository
) {

    //todo validator for startOn,endOn
    @Transactional
    fun create(rsvpDTO: RSVPDto): RSVPDto {
        val user = userService.getUser(rsvpDTO.username) ?: throw Error("user not found ${rsvpDTO.username}")
        val rsvp = rsvpRepository.save(RSVP.create(rsvpDTO, user, host, path))
        return RSVPDto.from(rsvp)
    }

    @Transactional(readOnly = true)
    fun getHistory(page: Int, size: Int, username: String): Pair<List<RSVPDto>, PageInfo> {
        val user = userService.getUser(username) ?: throw Error("user not found $username")
        val rsvps = rsvpRepository.findByUserAndIsActiveIs(
            user, false, PageRequest.of(page, size, Sort.by("createdOn").descending())
        )
        return Pair(RSVPDto.from(rsvps.content), PageInfo.from(rsvps))
    }

    @Transactional(readOnly = true)
    fun getActiveRSVPList(username: String): List<RSVPDto> {
        val user = userService.getUser(username) ?: throw Error("user not found $username")
        val rsvp = rsvpRepository.findByUserAndIsActiveIs(user, true)
        return RSVPDto.from(rsvp)
    }

    @Transactional(readOnly = true)
    fun getActiveRSVP(path: String): RSVPDto {
        val rsvp = rsvpRepository.findOneByLinkContains(path)?: throw Error("rsvp not found")

        if(!rsvp.isActive) {
            throw ServiceException(HttpStatus.BAD_REQUEST, ResultCode.RSVP_CLOSED, "rsvp is closed")
        }

        return RSVPDto.from(rsvp)
    }

    @Transactional(readOnly = false)
    fun close(path: String, username: String) {
        val user = userService.getUser(username) ?: throw Error("user not found $username")
        val rsvp = rsvpRepository.findOneByUserAndLinkContainsAndIsActive(user, path, true)
            ?: throw Error("cannot find. Perhaps, it is already closed?")
        rsvp.close()
    }

    @Transactional(readOnly = false)
    fun rsvp(guestRSVPRequest: GuestRSVPRequest): ResultCode {
        var code = ResultCode.SUCCESS
        val rsvp = rsvpRepository.findOneByLinkContains(guestRSVPRequest.path)

        if (rsvp == null) {
            code = ResultCode.RSVP_NOT_FOUND
        } else if (!rsvp.isActive) {
            code = ResultCode.RSVP_CLOSED
        } else {
            rsvp.responders.add(Responders.create(guestRSVPRequest, rsvp))
            rsvpRepository.save(rsvp)
        }

        return code
    }
}