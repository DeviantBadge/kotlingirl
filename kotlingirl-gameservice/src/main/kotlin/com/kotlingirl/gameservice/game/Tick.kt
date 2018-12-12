package com.kotlingirl.gameservice.game


import com.kotlingirl.gameservice.communication.Replica
import com.kotlingirl.gameservice.communication.MessageManager
import com.kotlingirl.gameservice.game.entities.Pawn
import com.kotlingirl.gameservice.game.entities.Tile
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.LockSupport

class Ticker {
    @Volatile
    var isWarm = true
    private val tickables = ConcurrentLinkedDeque<Tickable>()
    var tickNumber: Long = 0
    var pawns = HashMap<WebSocketSession, Pawn>()
    var tiles = HashMap<Point, Tile>()
    lateinit var messageManager: MessageManager

    fun gameLoop() {
        while (!Thread.currentThread().isInterrupted) {
            if (!isWarm) {
                initMain()
                isWarm = true
                messageManager.endWarm()
            }
            val started = System.currentTimeMillis()
            val replica = act(FRAME_TIME)
            val elapsed = System.currentTimeMillis() - started
            if (elapsed < FRAME_TIME) {
//                log.info("All tick finish at {} ms", elapsed)
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed))
            } else {
//                log.warn("tick lag {} ms", elapsed - FRAME_TIME)
            }
            broadcast(replica)
//            log.info("{}: tick ", tickNumber)
            tickNumber++
        }
    }

    fun initMain() {
        messageManager.mainInit()
    }

    fun registerTickable(tickable: Tickable) {
        tickables.add(tickable)
    }

    fun unregisterTickable(tickable: Tickable) {
        tickables.remove(tickable)
    }

    private fun act(elapsed: Long): Replica? {
//        tickables.forEach { it.tick(elapsed) }
        return messageManager.makeReplica(elapsed)
    }

    private fun broadcast(replica: Replica?) {
        messageManager.broadcastReplica(replica)
    }

    fun addPawn(session: WebSocketSession, pawn: Pawn) {
        pawns[session] = pawn
    }

   /* fun makeBG() {
        for (i in 0..16){
            tiles[Position(0, i * tileSize)] = Wall(i, Point(0, i * tileSize))
            tiles[Position(26 * tileSize, i * tileSize)] = Wall(i + 17, Point(26 * tileSize, i * tileSize))
        }
//        tiles[Position(0, 0)] = Wall(1, Point(0, 0))
//        broker.broadcast(Topic.REPLICA, Data(tiles.values.toList(), false))
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
    }*/

    companion object {
        private val log = logger()
        const val FPS = 60
        private const val FRAME_TIME = (1000 / FPS).toLong()

    }
}