package com.kotlingirl.gameservice.game


import com.kotlingirl.gameservice.communication.Replica
import com.kotlingirl.gameservice.communication.MessageManager
import com.kotlingirl.serverconfiguration.util.extensions.logger
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.LockSupport

class Ticker(val messageManager: MessageManager) {
    @Volatile
    var needReInit = false
    var tickNumber: Long = 0
    fun gameLoop() {
        while (!Thread.currentThread().isInterrupted) {
            if (needReInit) {
                initMainGame()
                needReInit = false
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

    private fun initMainGame() {
        messageManager.initMainGame()
    }

    private fun act(elapsed: Long): Replica? {
        return messageManager.makeReplica(elapsed)
    }

    private fun broadcast(replica: Replica?) {
        messageManager.broadcastReplica(replica)
    }

    companion object {
        private val log = logger()
        const val FPS = 60
        private const val FRAME_TIME = (1000 / FPS).toLong()

    }
}