package com.min.rsvp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class RsvpApplication

fun main(args: Array<String>) {
	runApplication<RsvpApplication>(*args)
}
