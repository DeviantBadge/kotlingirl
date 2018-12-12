package com.kotlingirl.gameservice.game

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class GameRepository {
    @Autowired
    private lateinit var beanFactory: BeanFactory

    // concurrent map
    private val games = ConcurrentHashMap<Int, Game>()
    init {
        games[0] = Game(3)
    }

    // get new game
    fun createGame(amount: Int): Game {
        val game = beanFactory.getBean(Game::class.java, amount)
        games[game.id] = game
        return game
    }

    // get ready game bean
    fun getGame(gameId: Int): Game? =
            games[gameId]
}