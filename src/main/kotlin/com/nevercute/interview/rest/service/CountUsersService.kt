package com.nevercute.interview.rest.service

import com.nevercute.interview.rest.domain.model.User
import org.springframework.stereotype.Component

@Component
class CountUsersService {

    companion object {
        private val users: List<User> = (1..10).map { User() }
    }

    fun countUsers(): Int = users.size
}
