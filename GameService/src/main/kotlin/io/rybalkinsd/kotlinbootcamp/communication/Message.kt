package io.rybalkinsd.kotlinbootcamp.communication

data class Message(val topic: Topic, val data: Any)

data class MoveData(val direction: String)
data class MoveMessage(val topic: Topic, val data: MoveData)