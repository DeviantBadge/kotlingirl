package com.kotlingirl.gameservice.game

val tileSize = 32

open class Tile(val id: Int, val position: Point) : Bar(position, Point(position.x + tileSize, position.y + tileSize)) {

}

class Wood(id: Int, position: Point) : Tile(id, position) {
    val type = "Wood"
}
class Wall(id: Int, position: Point) : Tile(id, position) {
    val type = "Wall"
}