package com.kotlingirl.gameservice.game.entities

import com.kotlingirl.gameservice.communication.BombDto
import com.kotlingirl.gameservice.game.Bar
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.gameservice.game.Tickable
import com.kotlingirl.gameservice.game.Ticker

class Bomb(val id: Int, val position: Point) : Tickable {
    var explosed = false
    val dto = BombDto(id, position)
    var timeLeft = TIME_LIVE
    val bar = Bar(Point(position.x + 8, position.y + 8), Point(position.x + 24, position.y + 24))
    override fun tick(elapsed: Long) {
        timeLeft--
    }

    companion object {
        const val TIME_LIVE = Ticker.FPS * 3
    }
}