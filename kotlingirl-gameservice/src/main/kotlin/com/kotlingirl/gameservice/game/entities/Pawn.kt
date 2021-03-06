package com.kotlingirl.gameservice.game.entities

import com.kotlingirl.gameservice.communication.PawnDto
import com.kotlingirl.gameservice.game.Bar
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.gameservice.game.Tickable

class Pawn(val count: Int) : Tickable {
    var id: Int = 0
        set(value) {
            field = value
            dto.id = value
        }
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
    var velocity = 1
    var bombsCount = 1
    var bombStrength = 1
    var deadTime = 100
    var steps = velocity
    override fun tick(elapsed: Long) {
        if (alive) {
            if (direction.isNotEmpty()) {
                when (direction) {
                    "UP" -> {
                        position.y += steps
                    }
                    "DOWN" -> {
                        position.y -= steps
                    }
                    "RIGHT" -> {
                        position.x += steps
                    }
                    "LEFT" -> {
                        position.x -= steps
                    }
                }
                updatePosition()
            } else {
                direction = ""
            }
        } else {
            deadTime--
        }

    }
    var dto = PawnDto(id, bar.leftBottomCorner, count = count)

    fun changePosition(newPosition: Point) {
        position = newPosition
        changeBarPosition()
        dto.position = position
        changeCoords()
    }

    fun reset() {
        velocity = 1
        bombsCount = 1
        bombStrength = 1
    }

    fun applyBonus(bonus: Bonus) {
        when(bonus) {
            is BonusBomb -> bombsCount++
            is BonusSpeed -> velocity++
            is BonusExplosion -> bombStrength++
        }
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

