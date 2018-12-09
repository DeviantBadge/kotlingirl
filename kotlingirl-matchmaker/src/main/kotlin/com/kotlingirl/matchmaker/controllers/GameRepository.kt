package com.kotlingirl.matchmaker.controllers

import com.kotlingirl.serverconfiguration.elements.matchmaker.MatchMakerGameUnit
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequest
import com.kotlingirl.serverconfiguration.elements.messages.UserRequestParameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Repository
class GameRepository {
    val games: Queue<MatchMakerGameUnit> = ConcurrentLinkedQueue()
    // todo ranked game holder

    @Autowired
    lateinit var serviceCommunicator: ServiceCommunicator

    fun getCasualGame(parameters: UserRequestParameters?) =
            games.poll() ?: serviceCommunicator.createCasualGame(parameters)

    fun appendPlayerToGame(game: MatchMakerGameUnit, credentials: UserCredentials):Unit =
            serviceCommunicator.appendPlayerToGame(game, credentials)


}