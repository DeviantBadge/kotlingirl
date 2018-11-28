package io.rybalkinsd.kotlinbootcamp.server

data class Game(val id:Int, val maxPlayers:Int = 2, var currentPlayerAmount:Int = 0) {
    fun isReady() =
            maxPlayers == currentPlayerAmount
}