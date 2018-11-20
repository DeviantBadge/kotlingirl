package io.rybalkinsd.kotlinbootcamp.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ChatApplication {
    @Bean
    fun standardSettings() = 2
}

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
