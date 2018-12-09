package com.kotlingirl.matchmaker

import com.kotlingirl.serverconfiguration.GlobalConstants.MATCHMAKER_RIBBON_CLIENT_NAME
import com.kotlingirl.serverconfiguration.ServerConfigurationApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.ribbon.RibbonClient
import com.kotlingirl.serverconfiguration.loadbalance.RibbonConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients


@EnableEurekaClient
@RibbonClient(name = MATCHMAKER_RIBBON_CLIENT_NAME, configuration = [RibbonConfiguration::class])
@SpringBootApplication(scanBasePackageClasses = [MatchMakerApplication::class, ServerConfigurationApplication::class])
class MatchMakerApplication

fun main(args: Array<String>) {
    runApplication<MatchMakerApplication>(*args)
}
