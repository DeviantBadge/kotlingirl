package com.kotlingirl.gameservice.http

import com.kotlingirl.gameservice.game.GameRepository
import com.kotlingirl.serverconfiguration.elements.messages.GameServiceResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequestParameters
import com.kotlingirl.serverconfiguration.proxies.gameservice.GameServiceControllerInterface
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
class GameServiceController: GameServiceControllerInterface {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    //  todo strange error while handle json
    override fun create(@RequestBody parameters: UserRequestParameters): ResponseEntity<GameServiceResponse> {
        // todo
        log.info("Haha, create data - $parameters")
        return ResponseEntity.ok().body(GameServiceResponse(0))
//        log.info("Added request for $playerCount players")
//        return when {
//            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player count")
//            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player count")
//            else -> {
//                // beanFactory create game and and handle errors
//                val game = gameRepository.createGame(playerCount)
//                ResponseEntity.ok(game.id.toString())
//            }
//        }
    }


    override fun connect(@RequestBody credentials: UserCredentials): ResponseEntity<String> {
        // todo
        log.info("Haha, connect data - $credentials")
        return ResponseEntity.ok().build()
//        return when {
//            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player count")
//            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player count")
//            else -> {
//                // beanFactory create game and and handle errors
//                val game = gameRepository.createGame(playerCount)
//                ResponseEntity.ok(game.id.toString())
//            }
//        }
    }
}
