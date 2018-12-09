package com.kotlingirl.registry

import com.kotlingirl.serverconfiguration.GlobalConstants.MATCHMAKER_RIBBON_CLIENT_NAME
import com.kotlingirl.serverconfiguration.ServerConfigurationApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.ribbon.RibbonClient
import com.kotlingirl.serverconfiguration.loadbalance.RibbonConfiguration


@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses = [KotlingirlRegistryApplication::class, ServerConfigurationApplication::class])
class KotlingirlRegistryApplication

fun main(args: Array<String>) {
    runApplication<KotlingirlRegistryApplication>(*args)
}
