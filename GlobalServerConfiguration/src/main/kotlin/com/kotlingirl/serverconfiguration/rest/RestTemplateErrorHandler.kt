package com.kotlingirl.serverconfiguration.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.Series.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler

@Component
class RestTemplateErrorHandler : ResponseErrorHandler {
    val log = LoggerFactory.getLogger(RestTemplateErrorHandler::class.java)!!

    override fun handleError(response: ClientHttpResponse) {
        log.info("We got error response with code ${response.statusCode} with message ${response.statusText}")
    }

    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode.series() == CLIENT_ERROR
                        || response.statusCode.series() == SERVER_ERROR
    }
}