package com.kotlingirl.front

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class KotlingirlFrontApplication {
}

fun main(args: Array<String>) {
    runApplication<KotlingirlFrontApplication>(*args)
}
