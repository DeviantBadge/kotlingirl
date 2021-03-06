package com.kotlingirl.gameservice.communication
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.serverconfiguration.util.IntIdGen
import org.springframework.web.socket.WebSocketSession

data class Replica(val topic: Topic, val data: Any)
data class Data(val objects: List<Any>, val gameOver: Boolean)
open class Message(open val topic: Topic, open val data: Any)
data class MoveMessage(override val topic: Topic, override val data: MoveData): Message(topic, Any())
data class MoveData(val direction: String)
data class User(val userId: Long) {
    var linked = false
    lateinit var webSocketSession: WebSocketSession
}
data class PawnDto(var id: Int, var position: Point = Point(0, 0),
                   var direction: String = "", val type: String = "Pawn",
                   var alive: Boolean = true, val count: Int) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class == PawnDto::class) return (other as PawnDto).id == id
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
data class BombDto(val id: Int, val position: Point, val type: String = "Bomb")
data class FireDto(val id: Int, val position: Point, val type: String = "Fire")