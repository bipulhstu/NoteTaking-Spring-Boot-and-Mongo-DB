package com.bipul.note_taking_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NoteTakingSpringBootAndMongoDbApplication

fun main(args: Array<String>) {
	runApplication<NoteTakingSpringBootAndMongoDbApplication>(*args)
}
