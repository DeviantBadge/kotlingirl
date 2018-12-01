package com.kotlingirl.serverconfiguration.proxies.gameservice

import com.kotlingirl.serverconfiguration.GameServiceConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(GameServiceConstants.GAME_PATH)
interface GameServiceControllerInterface {
    @PostMapping(
            path = [GameServiceConstants.CREATE_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody body: String): ResponseEntity<String>

    @PostMapping(
            path = [GameServiceConstants.CONNECT_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun connect(@RequestBody body: String): ResponseEntity<String>

    @PostMapping(
            path = [GameServiceConstants.START_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun start(@RequestBody body: String): ResponseEntity<String>
}
