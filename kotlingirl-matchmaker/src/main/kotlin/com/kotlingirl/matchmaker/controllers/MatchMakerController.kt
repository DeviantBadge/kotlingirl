package com.kotlingirl.matchmaker.controllers

import com.kotlingirl.serverconfiguration.MatchMakerConstants
import com.kotlingirl.serverconfiguration.elements.InternalException
import com.kotlingirl.serverconfiguration.elements.messages.MatchMakerGameResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequest
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

// todo read about spring security
// todo read about spring services
// todo read about services communication
// todo read about oath2
// todo read about encryption
// todo implement basis

@Controller
@RequestMapping(MatchMakerConstants.PLAY_PATH)
class MatchMakerController {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var serviceCommunicator: ServiceCommunicator

    // here can be an exception, its on first time
    // todo create loop for several attempts (stops when retry amount is very high or time was more than default)
    @PostMapping(
            path = [MatchMakerConstants.CASUAL_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun casual(@RequestBody request: UserRequest): ResponseEntity<MatchMakerGameResponse> {
        checkUser(request.userId)
        val game = gameRepository.getCasualGame(request.parameters)
        try {
            gameRepository.appendPlayerToGame(game, request.userId!!)
        } catch (e: Exception) {
            gameRepository.putBackIfNotReady(game)
            throw e
        }
        gameRepository.putBackIfNotReady(game)
        return ResponseEntity.ok().body(MatchMakerGameResponse(game.serviceInstance.instanceId, game.id))
    }

    @PostMapping(
            path = [MatchMakerConstants.RANKED_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun ranked(@RequestBody request: UserRequest): ResponseEntity<MatchMakerGameResponse> {
        TODO()
    }

    @RequestMapping("/greeting")
    fun test(): ResponseEntity<String> {
        log.warn("GOT IT, I GOT REQUEST!")
        return ResponseEntity.ok().build()
    }

    fun checkUser(userId: Long?): Unit = when {
        userId == null -> throw InternalException(HttpStatus.BAD_REQUEST, "Request without credentials")
        else -> {
            serviceCommunicator.checkPlayerId(userId)
        }
    }
}
