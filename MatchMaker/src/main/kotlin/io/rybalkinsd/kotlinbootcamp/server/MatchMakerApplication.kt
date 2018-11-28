package io.rybalkinsd.kotlinbootcamp.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients("io.rybalkinsd.kotlinbootcamp.server.proxies")
@EnableDiscoveryClient
class MatchMakerApplication {
}

fun main(args: Array<String>) {
    runApplication<MatchMakerApplication>(*args)
}
