package com.kotlingirl.serverconfiguration.proxies.gameservice

import com.kotlingirl.serverconfiguration.GameServiceConstants
import com.kotlingirl.serverconfiguration.elements.messages.GameServiceResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequestParameters
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(GameServiceConstants.GAME_PATH)
interface GameServiceControllerInterface {
    @PostMapping(
            path = [GameServiceConstants.CREATE_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody parameters: UserRequestParameters): ResponseEntity<GameServiceResponse>

    @PostMapping(
            path = [GameServiceConstants.APPEND_PLAYER_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun connect(@RequestBody credentials: UserCredentials): ResponseEntity<String>
}
