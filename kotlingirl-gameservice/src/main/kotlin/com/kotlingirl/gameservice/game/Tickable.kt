package com.kotlingirl.gameservice.game

interface Tickable {
    fun tick(elapsed: Long)
}