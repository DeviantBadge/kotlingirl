package com.kotlingirl.serverconfiguration.util.extensions

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun String.toJsonHttpEntity() =
        HttpEntity(
                this,
                HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON })

fun Any.toJsonHttpEntity() =
        HttpEntity(
                this.toJsonString(),
                HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON })
