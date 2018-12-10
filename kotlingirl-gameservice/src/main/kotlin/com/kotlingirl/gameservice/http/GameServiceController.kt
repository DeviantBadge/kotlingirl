package com.kotlingirl.gameservice.http

import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.gameservice.game.GameRepository
import com.kotlingirl.serverconfiguration.GameServiceConstants
import com.kotlingirl.serverconfiguration.elements.messages.GameServiceResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequestParameters
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping(GameServiceConstants.GAME_PATH)
class GameServiceController {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    @PostMapping(
            path = [GameServiceConstants.CREATE_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody parameters: UserRequestParameters): ResponseEntity<GameServiceResponse> {
        // todo integrate with user parameters
        log.info("Haha, create data - $parameters")
        // log.info("Added request for $playerCount players")
        val game = gameRepository.createGame(3)
        return ResponseEntity.ok().body(GameServiceResponse(game.id))
//        ResponseEntity.ok(game.id.toString())
//        return when {
//            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player pawnCount")
//            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player pawnCount")
//            else -> {
//                // beanFactory create game and and handle errors
//                val game = gameRepository.createGame(3)
//                ResponseEntity.ok(game.id.toString())
//            }
//        }
    }

    @PostMapping(
            path = [GameServiceConstants.APPEND_PLAYER_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    // todo
    fun appendPlayer(@RequestParam("id") gameId: Int,
                     @RequestBody credentials: UserCredentials): ResponseEntity<String> {
        val game = gameRepository.getGame(gameId)
        game!!.addUser(User(credentials.name ?: ""))
        log.info("Haha, connect data - $credentials")
        return ResponseEntity.ok().build()
    }
}
