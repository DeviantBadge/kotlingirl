package com.kotlingirl.gameservice.http

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
        // todo
        log.info("Haha, create data - $parameters")
        return ResponseEntity.ok().body(GameServiceResponse(0))
//        log.info("Added request for $playerCount players")
//        return when {
//            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player pawnCount")
//            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player pawnCount")
//            else -> {
//                // beanFactory create game and and handle errors
//                val game = gameRepository.createGame(playerCount)
//                ResponseEntity.ok(game.id.toString())
//            }
//        }
    }


    @PostMapping(
            path = [GameServiceConstants.APPEND_PLAYER_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun connect(@RequestBody credentials: UserCredentials): ResponseEntity<String> {
        // todo
        log.info("Haha, connect data - $credentials")
        return ResponseEntity.ok().build()
//        return when {
//            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player pawnCount")
//            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player pawnCount")
//            else -> {
//                // beanFactory create game and and handle errors
//                val game = gameRepository.createGame(playerCount)
//                ResponseEntity.ok(game.id.toString())
//            }
//        }
    }
}
