package com.kotlingirl.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class KotlingirlGatewayApplication

fun main(args: Array<String>) {
    runApplication<KotlingirlGatewayApplication>(*args)
}
