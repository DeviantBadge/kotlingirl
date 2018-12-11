package com.kotlingirl.gameservice

import com.kotlingirl.serverconfiguration.ServerConfigurationApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean

@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses = [GameServiceApplication::class, ServerConfigurationApplication::class])
class GameServiceApplication {
    @Bean
    fun amount() = 3
}

fun main(args: Array<String>) {

    runApplication<GameServiceApplication>(*args)
}
