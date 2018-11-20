package io.rybalkinsd.kotlinbootcamp.server

import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import java.util.Queue

@SpringBootApplication
class ChatApplication {
}

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
