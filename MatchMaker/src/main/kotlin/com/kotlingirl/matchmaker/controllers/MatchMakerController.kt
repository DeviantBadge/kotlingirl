package com.kotlingirl.matchmaker.controllers

import com.kotlingirl.serverconfiguration.elements.InternalException
import com.kotlingirl.serverconfiguration.elements.messages.UserRequest
import com.kotlingirl.serverconfiguration.proxies.matchmaker.MatchMakerControllerInterface
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
class MatchMakerController: MatchMakerControllerInterface{
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    // todo read about spring security
    // todo read about spring services
    // todo read about services communication
    // todo read about oath2
    // todo read about encryption
    // todo implement basis
    override fun casual(@RequestBody body: String): ResponseEntity<String> {
        val clientRequest = body.fromJsonString(UserRequest::class.java)
        val game = gameRepository.getCasualGame(clientRequest)
        gameRepository.injectPlayerToGame(game, clientRequest)
        TODO()
    }

    override fun ranked(@RequestBody body: String): ResponseEntity<String> {
        TODO()
    }

    fun checkUser(name: String?): Unit = when {
        name == null -> throw InternalException(HttpStatus.BAD_REQUEST, "Request without name")
        name.length < 3 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too short name")
        name.length > 20 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too Long Name")
        else -> {
        }
    }
}
