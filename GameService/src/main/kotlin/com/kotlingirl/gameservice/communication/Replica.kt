package com.kotlingirl.gameservice.communication

data class Replica(val topic: String, val data: Data)
data class Data(val objects: List<Any>, val gameOver: Boolean)