package io.rybalkinsd.kotlinbootcamp.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GameServiceApplication {
    @Bean
    fun standardSettings(): Int = 2
}

fun main(args: Array<String>) {
    runApplication<GameServiceApplication>(*args)
}
