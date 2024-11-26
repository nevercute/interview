package com.nevercute.interview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
