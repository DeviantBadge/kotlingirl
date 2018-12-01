package com.kotlingirl.serverconfiguration.rest

import com.alibaba.fastjson.JSONException
import com.kotlingirl.serverconfiguration.elements.InternalException
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    val log = logger()

    @ExceptionHandler(value = [InternalException::class])
    protected fun handleInternalException(ex: InternalException, request: WebRequest): ResponseEntity<String> {
        log.warn("Caught InternalException with parameters " +
                "status: ${ex.status} and " +
                "message: ${ex.message}")
        return ResponseEntity.status(ex.status).body(ex.message)
    }

    @ExceptionHandler(value = [JSONException::class])
    protected fun handleJsonException(ex: JSONException, request: WebRequest): ResponseEntity<String> {
        log.warn("Caught json error}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant parse requested json")
    }
}
