package com.kotlingirl.gameservice.communication

import com.kotlingirl.gameservice.game.Point

data class Message(val topic: Topic, val data: Data)
data class Data(val objects: List<Any>, val gameOver: Boolean)
data class User(val id: Int, val name: String)
data class PawnDto(var id: Int, var position: Point = Point(0, 0), var direction: String = "", val type: String = "Pawn")