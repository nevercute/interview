package com.nevercute.interview.it.rest

import com.nevercute.interview.rest.dto.UsersCountResponse
import com.nevercute.interview.it.BaseRestIT
import io.mockk.every
import mu.KLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.util.StopWatch

class RestControllerTest: BaseRestIT() {

    companion object: KLogging() {
       const val EXPECTED_USERS_COUNT = 10
    }

    @BeforeEach
    fun init() {
        every { countUsersService.countUsers() }.answers { EXPECTED_USERS_COUNT }
    }

    @Test
    @DisplayName("Должны вернуть количество пользователей в ответе")
    fun shouldReturnUsersCountResponse() {
        val watch = StopWatch()
        val expectation = UsersCountResponse(10)
        watch.start("countUsers watcher task")
        mockMvc.post("/countUsers") {
            contentType = MediaType.APPLICATION_JSON
            content = null
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectation)) }
        }
        watch.stop()
        logger.info { "Работа выполнена работа контроллера, данные выполнения: ${watch.prettyPrint()}" }
    }
}
