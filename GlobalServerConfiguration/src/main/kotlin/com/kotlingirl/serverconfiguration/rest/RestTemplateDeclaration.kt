package com.kotlingirl.serverconfiguration.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateDeclaration {
    @Autowired
    lateinit var errorHandler: RestTemplateErrorHandler

    @Autowired
    lateinit var restTemplateBuilder: RestTemplateBuilder

    @Bean(name = ["no_exc"])
    fun restWithoutExceptions(): RestTemplate {
        return restTemplateBuilder
                .errorHandler(errorHandler)
                .build()
    }
}