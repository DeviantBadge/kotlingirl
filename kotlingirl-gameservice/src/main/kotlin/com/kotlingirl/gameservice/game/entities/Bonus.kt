package com.kotlingirl.gameservice.game.entities

import com.kotlingirl.gameservice.game.Bar
import com.kotlingirl.gameservice.game.Point

open class Bonus(val id: Int, val position: Point) : Bar(position, Point(position.x + tileSize, position.y + tileSize)) {
    var taken = false
    val type = "Bonus"
}
class BonusBomb(  id: Int,   position: Point) : Bonus(id, position) {
    val bonusType = "BOMBS"
}
class BonusExplosion(  id: Int,  position: Point) : Bonus(id, position) {
    val bonusType = "RANGE"
}
class BonusSpeed(  id: Int,   position: Point) : Bonus(id, position) {
    val bonusType = "SPEED"
}
