package io.rybalkinsd.kotlinbootcamp.server.matchmaker

import io.rybalkinsd.kotlinbootcamp.server.Game
import io.rybalkinsd.kotlinbootcamp.server.HttpClient
import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class MatchMakerController {
    val log = logger()

    val games: Queue<Game> = ConcurrentLinkedQueue()

    @Autowired
    lateinit var client: HttpClient

    // todo read about spring security
    // todo read about spring services
    // todo read about services communication
    // todo read about oath2
    // todo read about encryption
    // todo implement basis
    @RequestMapping(
            path = ["/play"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun play(@RequestParam("name") name: String): ResponseEntity<String> {
        val checkResult = checkUser(name)
        if (checkResult != null)
            return checkResult
        var game = games.poll()
        if (game == null) {
            val gameId = getGameId(2)
                    ?: return ResponseEntity.status(500).body("Failed to get Game instance Id")
            game = Game(gameId)
        }
        game.currentPlayerAmount++
        if (game.isReady())
            startGame(game.id)
        else
            games.add(game)

        return ResponseEntity.ok(game.id.toString())
    }

    private fun startGame(id: Int) {
        client.startGame(id)
    }

    private fun getGameId(count: Int): Int? {
        return client.getGameId(count)
    }

    fun checkUser(name: String): ResponseEntity<String>? = when {
        name.length < 3 -> ResponseEntity.badRequest().body("Too Short Name")
        name.length > 20 -> ResponseEntity.badRequest().body("Too Long Name")
        else -> null
    }
}
