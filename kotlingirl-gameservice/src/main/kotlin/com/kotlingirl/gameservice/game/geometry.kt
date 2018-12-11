package com.kotlingirl.gameservice.game

import com.alibaba.fastjson.annotation.JSONField
import kotlin.math.max
import kotlin.math.min

/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
}

/**
 * 2D point with integer coordinates
 */
data class Point(var x: Int = 0, var y: Int = 0) : Collider {
    override fun isColliding(other: Collider) = when (other::class) {
        Point::class -> this == other
        Bar::class -> this in other as Bar

        else -> throw NotImplementedError("Unknown collider type - " + other::class::simpleName)
    }
}

/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
open class Bar(firstCornerX: Int, firstCornerY: Int, secondCornerX: Int, secondCornerY: Int) : Collider {
    @JSONField(serialize = false)
    var leftBottomCorner = Point(min(firstCornerX, secondCornerX), min(firstCornerY, secondCornerY))
    @JSONField(serialize = false)
    var rightTopCorner = Point(max(firstCornerX, secondCornerX), max(firstCornerY, secondCornerY))

    constructor(firstCorner: Point, secondCorner: Point) :
            this(firstCorner.x, firstCorner.y, secondCorner.x, secondCorner.y)

    operator fun contains(point: Point) =
            point.x <= rightTopCorner.x &&
                    point.x >= leftBottomCorner.x &&
                    point.y <= rightTopCorner.y &&
                    point.y >= leftBottomCorner.y

    fun isPerimeters(bar: Bar) =
            bar.leftBottomCorner.x <= rightTopCorner.x &&
                    bar.rightTopCorner.x >= leftBottomCorner.x &&
                    bar.leftBottomCorner.y <= rightTopCorner.y &&
                    bar.rightTopCorner.y >= leftBottomCorner.y

    override fun equals(other: Any?) = when (other) {
        null -> false
        super.equals(other) -> true
        other::class == Point::class -> leftBottomCorner == other && rightTopCorner == other
        other::class == Bar::class -> leftBottomCorner == (other as Bar).leftBottomCorner &&
                rightTopCorner == other.rightTopCorner

        else -> false
    }

    override fun isColliding(other: Collider) = when (other) {
        is Point -> other in this
        is Bar -> this.isPerimeters(other)

        else -> throw NotImplementedError("Unknown collider type - ${other::class::simpleName}")
    }
}