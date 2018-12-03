package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.toJsonString

val pawnSize = 32

data class Pawn(val id: Int, var position: Position, var alive: Boolean, var direction: String)
    : Bar(Point(position.x, position.y), Point(position.x + pawnSize, position.y + pawnSize)), Tickable {
    val type = "Pawn"
    var steps = 0
    var nearestTile : Tile? = null
    override fun tick(elapsed: Long) {
        if (direction.isNotEmpty()) {
            val velocity = 1
            steps--
            if (steps == 0) {
                if (nearestTile?.isColliding(this) != true) {
                    when (direction) {
                        "UP" -> {
                            position.y += velocity
                        }
                        "DOWN" -> {
                            position.y -= velocity
                        }
                        "RIGHT" -> {
                            position.x += velocity
                        }
                        "LEFT" -> {
                            position.x -= velocity
                        }
                    }
                    leftBottomCorner = Point(position.x, position.y)
                    rightTopCorner = Point(position.x + pawnSize, position.y + pawnSize)
                }
                steps = 2
            }
        } else {
            direction = ""
        }
    }
}

data class Position(var x: Int, var y: Int)

data class User(val id: Int, val name: String)

fun main(args: Array<String>) {
    val pawn = Pawn(12, Position(10,10),true, "RIGHT")
    println(listOf(pawn, pawn, pawn))
    println(listOf(pawn, pawn, pawn).toJsonString())
    println(listOf(pawn, pawn, pawn).toJsonString().fromJsonString(List::class.java))
}