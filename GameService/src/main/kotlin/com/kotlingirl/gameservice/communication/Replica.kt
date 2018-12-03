package io.rybalkinsd.kotlinbootcamp.communication

import io.rybalkinsd.kotlinbootcamp.game.Tickable

data class Replica(val topic: String, val data: Data)
data class Data(val objects: List<Any>, val gameOver: Boolean)