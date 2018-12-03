package com.kotlingirl.gameservice.game


import com.kotlingirl.gameservice.communication.Broker
import com.kotlingirl.gameservice.communication.Data
import com.kotlingirl.gameservice.communication.Message
import com.kotlingirl.gameservice.communication.MoveData
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import com.kotlingirl.serverconfiguration.util.extensions.toJsonString
import com.kotlingirl.gameservice.communication.Topic
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.LockSupport

class Ticker {
    private val tickables = ConcurrentLinkedDeque<Tickable>()
    var tickNumber: Long = 0

    var inputQueue = ConcurrentLinkedQueue<Pair<WebSocketSession, Message> >()
    var pawns = HashMap<WebSocketSession, Pawn>()
    var tiles = HashMap<Position, Tile>()

    lateinit var broker: Broker

    fun gameLoop() {
        while (!Thread.currentThread().isInterrupted) {
            val started = System.currentTimeMillis()
            var data = consumeMessage()
            //act(FRAME_TIME)
            val elapsed = System.currentTimeMillis() - started
            if (elapsed < FRAME_TIME) {
                log.info("All tick finish at {} ms", elapsed)
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed))
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME)
            }
            log.info("{}: tick ", tickNumber)
            tickNumber++
            if (data != null) {
                broker.broadcast(data.first, data.second)
            }
        }
    }

    fun registerTickable(tickable: Tickable) {
        tickables.add(tickable)
    }

    fun unregisterTickable(tickable: Tickable) {
        tickables.remove(tickable)
    }

    private fun act(elapsed: Long) {
        tickables.forEach { it.tick(elapsed) }
    }

    fun input(session: WebSocketSession, msg: Message) {
        inputQueue.add(Pair(session, msg))
    }

    fun consumeMessage(): Pair<Topic, Data>? {
        if (inputQueue.isNotEmpty()) {
            val size = inputQueue.size
            log.info("Internal of copied queue: size = ${size}")
            val lst = mutableListOf<Any>()
            for (i in 0..(size - 1))
             {
                val pair = inputQueue.poll()
                if (pair.second.topic == Topic.MOVE) {
                    val moveData: MoveData = pair.second.data.toJsonString().fromJsonString()
                    val pawn = pawns[pair.first]
                    if(pawn != null) {
                        if (pawn.direction != moveData.direction) {
                            pawn.direction = moveData.direction
                            pawn.steps = 2
                            setNearestTile(pawn)
                        }
                        pawn.tick(FRAME_TIME)
                        lst.add(pawn)
                    }
                }
            }
            if (lst.isNotEmpty()) {
                log.info("last state of pawn ${lst.last()}")
                val replica = Data(lst, false)
                return Pair(Topic.REPLICA, replica)
            } else {
                return null
            }
        }
        return null
    }

    fun addPawn(session: WebSocketSession, pawn: Pawn) {
        pawns[session] = pawn
    }

    fun makeBG() {
        for (i in 0..16){
            tiles[Position(0, i * tileSize)] = Wall(i, Point(0, i * tileSize))
            tiles[Position(26 * tileSize, i * tileSize)] = Wall(i + 17, Point(26 * tileSize, i * tileSize))
        }
//        tiles[Position(0, 0)] = Wall(1, Point(0, 0))
        broker.broadcast(Topic.REPLICA, Data(tiles.values.toList(), false))
    }

    fun setNearestTile(pawn: Pawn) {

        when (pawn.direction) {
            "UP" -> {
                val hypX = pawn.position.x / tileSize * tileSize
                val minY = pawn.position.y / tileSize * tileSize
                val nearestTile = tiles.filter { it -> it.key.x == hypX && it.key.y > minY }.minBy { it.key.y }?.value
                pawn.nearestTile = nearestTile
            }
            "DOWN" -> {
                val hypX = pawn.position.x / tileSize * tileSize
                val maxY = pawn.position.y / tileSize * tileSize
                val nearestTile = tiles.filter { it -> it.key.x == hypX && it.key.y < maxY }.maxBy { it.key.y }?.value
                pawn.nearestTile = nearestTile
            }
            "RIGHT" -> {
                val hypY = pawn.position.y / tileSize * tileSize
                val minX = pawn.position.x / tileSize * tileSize
                val nearestTile = tiles.filter { it -> it.key.y == hypY && it.key.x > minX }.minBy { it.key.y }?.value
                pawn.nearestTile = nearestTile
            }
            "LEFT" -> {
                val hypY = pawn.position.y / tileSize * tileSize
                val maxX = pawn.position.x / tileSize * tileSize
                val nearestTile = tiles.filter { it -> it.key.y == hypY && it.key.x < maxX }.maxBy { it.key.y }?.value
                pawn.nearestTile = nearestTile
            }
            else -> pawn.nearestTile = null
        }
    }

    companion object {
        private val log = logger()
        private const val FPS = 60
        private const val FRAME_TIME = (1000 / FPS).toLong()
    }
}