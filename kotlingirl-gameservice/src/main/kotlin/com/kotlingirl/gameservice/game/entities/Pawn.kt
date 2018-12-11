package com.kotlingirl.gameservice.game.entities

import com.kotlingirl.gameservice.communication.PawnDto
import com.kotlingirl.gameservice.game.Bar
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.gameservice.game.Tickable

class Pawn(val id: Int, val count: Int) : Tickable {

    var position = Point(0, 0)
    val pawnSize = 32
    val halfpawnSize = pawnSize / 2
    val quartPawnSize = pawnSize / 4
    var alive: Boolean = true
        set(value) {
            field = value
            dto.alive = value
        }
    var direction: String = ""
        set(value) {
            field = value
            dto.direction = value
        }
    var coords = mutableSetOf<Point>()
    var bar = Bar(position, position)
    val velocity = 2
    var bombsCount = 1

    override fun tick(elapsed: Long) {
        if (alive) {
            if (direction.isNotEmpty()) {
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
                updatePosition()
            } else {
                direction = ""
            }
        }

    }
    var dto = PawnDto(id, bar.leftBottomCorner, count = count)

    fun changePosition(newPosition: Point) {
        position = newPosition
        changeBarPosition()
        dto.position = position
        changeCoords()
    }

    private fun updatePosition() {
        changeBarPosition()
        changeCoords()
        dto.position = position
    }

    private fun changeBarPosition() {
        bar.leftBottomCorner = Point(position.x + quartPawnSize, position.y)
        bar.rightTopCorner = Point(position.x + 3 * quartPawnSize, position.y + halfpawnSize)
    }

    private fun changeCoords() {
        coords.clear()
        val leftX = bar.leftBottomCorner.x / pawnSize
        val bottomY = bar.leftBottomCorner.y / pawnSize
        val rightX = bar.rightTopCorner.x / pawnSize
        val topY = bar.rightTopCorner.y / pawnSize
        coords.add(Point(leftX, bottomY))
        if (leftX == rightX) {
            if (bottomY != topY) {
                coords.add(Point(leftX, topY))
            }
        } else {
            coords.add(Point(rightX, bottomY))
            if (bottomY != topY) {
                coords.add(Point(leftX, topY))
                coords.add(Point(rightX, topY))
            }
        }
    }

}

