package com.kotlingirl.gameservice.game

data class Pawn(val id: Int) : Tickable {
    val pawnSize = 32
    var position = Point(0, 0)
    var alive: Boolean = true
    var direction: String = ""
    var bar = Bar(Point(position.x, position.y), Point(position.x + pawnSize, position.y + pawnSize))
    var steps = 0
    var nearestTile : Tile? = null
    override fun tick(elapsed: Long) {
        if (direction.isNotEmpty()) {
            val velocity = 1
            steps--
            if (steps == 0) {
                if (nearestTile?.isColliding(bar) != true) {
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
                    bar.leftBottomCorner = Point(position.x, position.y)
                    bar.rightTopCorner = Point(position.x + pawnSize, position.y + pawnSize)
                }
                steps = 2
            }
        } else {
            direction = ""
        }
    }
}

