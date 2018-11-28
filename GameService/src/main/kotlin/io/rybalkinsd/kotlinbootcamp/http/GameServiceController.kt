package io.rybalkinsd.kotlinbootcamp.http

import io.rybalkinsd.kotlinbootcamp.game.GameRepository
import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/game")
class GameServiceController {
    val log = logger()

    @Autowired
    lateinit var gameRepository: GameRepository

    //  todo make with json everywhere
    @PostMapping(
            path = ["/create"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun create(@RequestParam("playerCount") playerCount: Int):
            ResponseEntity<String> {
        log.info("Added request for $playerCount players")
        return when {
            playerCount < 2 -> ResponseEntity.badRequest().body("Too low player count")
            playerCount > 4 -> ResponseEntity.badRequest().body("Too high player count")
            else -> {
                // beanFactory create game and and handle errors
                val game = gameRepository.createGame(playerCount)
                ResponseEntity.ok(game.id.toString())
            }
        }
    }

    @GetMapping(
            path = ["/create"],
            consumes = [MediaType.ALL_VALUE]
    )
    fun createe(@RequestParam("playerCount") playerCount: Int):
            ResponseEntity<String> = when {
        playerCount < 2 -> ResponseEntity.badRequest().body("Too low player count")
        playerCount > 4 -> ResponseEntity.badRequest().body("Too high player count")
        else -> {
            // beanFactory create game and and handle errors
            val game = gameRepository.createGame(playerCount)
            ResponseEntity.ok(game.id.toString())
        }
    }

    //  todo make with json everywhere
    @RequestMapping(
            path = ["/start"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun start(@RequestParam("gameId") gameId: Int):
            ResponseEntity<String> {
        val game = gameRepository.getGame(gameId)
                ?: return ResponseEntity.badRequest().body("Unknown game id - $gameId")
        game.start()
        return ResponseEntity.ok().build()
    }
}
