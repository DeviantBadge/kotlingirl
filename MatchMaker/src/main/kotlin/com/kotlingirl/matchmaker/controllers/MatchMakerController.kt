package com.kotlingirl.matchmaker.controllers

import com.kotlingirl.serverconfiguration.elements.InternalException
import com.kotlingirl.serverconfiguration.elements.messages.MatchMakerGameResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequest
import com.kotlingirl.serverconfiguration.proxies.matchmaker.MatchMakerControllerInterface
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

// todo read about spring security
// todo read about spring services
// todo read about services communication
// todo read about oath2
// todo read about encryption
// todo implement basis

@Controller
class MatchMakerController : MatchMakerControllerInterface {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    override fun casual(@RequestBody request: UserRequest): ResponseEntity<MatchMakerGameResponse> {
        checkUser(request.credentials)
        val game = gameRepository.getCasualGame(request.parameters)
        // here can be an exception, its on first time
        // todo create loop for several attempts (stops when retry amount is very high or time was more than default)
        gameRepository.appendPlayerToGame(game, request.credentials!!)
        return ResponseEntity.ok().body(MatchMakerGameResponse(game.serviceInstance.uri, game.id))
    }

    override fun ranked(@RequestBody request: UserRequest): ResponseEntity<MatchMakerGameResponse> {
        TODO()
    }

    fun checkUser(credentials: UserCredentials?): Unit = when {
        credentials == null -> throw InternalException(HttpStatus.BAD_REQUEST, "Request without credentials")
        credentials.name == null -> throw InternalException(HttpStatus.BAD_REQUEST, "Request without name")
        credentials.name!!.length < 3 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too short name")
        credentials.name!!.length > 20 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too Long Name")
        else -> {
        }
    }
}