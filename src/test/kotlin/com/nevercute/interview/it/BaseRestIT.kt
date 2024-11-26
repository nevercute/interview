package com.nevercute.interview.it

import com.fasterxml.jackson.databind.ObjectMapper
import com.nevercute.interview.rest.service.CountUsersService
import com.ninjasquad.springmockk.MockkBean
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner::class)
@WebMvcTest
abstract class BaseRestIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var countUsersService: CountUsersService
}
