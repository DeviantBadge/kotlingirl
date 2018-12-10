package com.kotlingirl.gameservice.game.entities

import com.kotlingirl.gameservice.communication.FireDto
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.gameservice.game.Tickable

class Fire(val id: Int, val position: Point) : Tickable {

    var leftTime = 5
    val dto = FireDto(id, position)
    override fun tick(elapsed: Long) {
        leftTime--
    }
}