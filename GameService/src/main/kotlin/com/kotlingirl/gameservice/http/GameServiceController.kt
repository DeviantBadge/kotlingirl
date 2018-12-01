package com.kotlingirl.gameservice.http

import com.kotlingirl.gameservice.game.GameRepository
import com.kotlingirl.serverconfiguration.proxies.GameServiceControllerInterface
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class GameServiceController: GameServiceControllerInterface {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    //  todo make with json everywhere
    override fun create(@RequestBody body: String): ResponseEntity<String> {
        // todo
        return ResponseEntity.ok().body("Haha, create data - $body")
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


    override fun connect(@RequestBody body: String): ResponseEntity<String> {
        // todo
        return ResponseEntity.ok().body("Haha, connect data - $body")
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

    //  todo make with json everywhere
    override fun start(@RequestBody body: String): ResponseEntity<String> {
        // todo
        return ResponseEntity.ok().body("Haha, start data - $body")
//        val game = gameRepository.getGame(gameId)
//                ?: return ResponseEntity.badRequest().body("Unknown game id - $gameId")
//        game.start()
//        return ResponseEntity.ok().build()
    }
}
