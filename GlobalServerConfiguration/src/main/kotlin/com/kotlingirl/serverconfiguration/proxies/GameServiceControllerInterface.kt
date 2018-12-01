package com.kotlingirl.serverconfiguration.proxies

import com.kotlingirl.serverconfiguration.GameServiceConstants.CONNECT_PATH
import com.kotlingirl.serverconfiguration.GameServiceConstants.CREATE_PATH
import com.kotlingirl.serverconfiguration.GameServiceConstants.GAME_PATH
import com.kotlingirl.serverconfiguration.GameServiceConstants.START_PATH
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(GAME_PATH)
interface GameServiceControllerInterface {
    @PostMapping(
            path = [CREATE_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody body: String): ResponseEntity<String>

    @PostMapping(
            path = [CONNECT_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun connect(@RequestBody  body: String): ResponseEntity<String>

    @PostMapping(
            path = [START_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun start(@RequestBody body: String): ResponseEntity<String>
}
