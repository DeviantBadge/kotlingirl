package io.rybalkinsd.kotlinbootcamp.game

data class Pawn(val id: Int, var position: Position, var alive: Boolean, var direction: String) : Tickable {
    val type = "Pawn"
    var steps = 0
    override fun tick(elapsed: Long) {
        if (direction.isNotEmpty()) {
            val velocity = 1
            steps--
            if (steps == 0) {
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
                steps = 2
            }
        } else {
            direction = ""
        }
    }
}

data class Position(var x: Int, var y: Int)

data class User(val id: Int, val name: String)
