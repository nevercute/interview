package com.nevercute.interview.rest.domain.model

import java.util.UUID

data class User(
    val userId: String = UUID.randomUUID().toString()
)
