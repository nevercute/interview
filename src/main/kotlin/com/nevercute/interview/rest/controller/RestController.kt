package com.nevercute.interview.rest.controller

import com.nevercute.interview.rest.dto.UsersCountResponse
import com.nevercute.interview.rest.service.CountUsersService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController(
    private val countUsersService: CountUsersService
) {

    @PostMapping("/countUsers", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    @RequestMapping(method = [RequestMethod.HEAD])
    fun countUsers(): UsersCountResponse {
        return UsersCountResponse(countUsersService.countUsers())
    }
}
