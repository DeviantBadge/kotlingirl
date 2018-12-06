package com.kotlingirl.gameservice.game

import com.kotlingirl.gameservice.communication.BombDto

class Bomb(val id: Int, val position: Point) : Tickable {

    val dto = BombDto(id, position)
    var timeLeft = TIME_LIVE
    override fun tick(elapsed: Long) {
        timeLeft--
    }

    companion object {
        const val TIME_LIVE = Ticker.FPS * 5
    }
}