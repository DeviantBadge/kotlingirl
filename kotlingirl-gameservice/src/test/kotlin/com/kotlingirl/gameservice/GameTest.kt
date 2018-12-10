package com.kotlingirl.gameservice

import com.kotlingirl.gameservice.communication.Broker
import com.kotlingirl.gameservice.communication.ConnectionPool
import com.kotlingirl.gameservice.communication.Message
import com.kotlingirl.gameservice.communication.MessageManager
import com.kotlingirl.gameservice.communication.MoveData
import com.kotlingirl.gameservice.communication.Topic
import com.kotlingirl.gameservice.communication.User
import com.kotlingirl.gameservice.game.Mechanics
import com.kotlingirl.gameservice.game.entities.Pawn
import com.kotlingirl.gameservice.game.Point
import com.kotlingirl.serverconfiguration.util.extensions.logger
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.springframework.web.socket.WebSocketSession

class GameTest {

    companion object {
        private val log = logger()
        private const val FPS = 60
        private const val FRAME_TIME = (1000 / FPS).toLong()
    }

    @Before
    fun init() {

    }

    @Test
    fun `one move test`() {
        val session = mock(WebSocketSession::class.java)
        val mechanics = Mechanics()
        val connectionPool = ConnectionPool()
        val broker = Broker(connectionPool)
        val messageManager = MessageManager(mechanics, broker)
        mechanics.createPawn(session, User(0, ""))
        mechanics.initPawns()
        val message: Message = Message(Topic.MOVE, MoveData(direction = "UP"))
        messageManager.addMessage(session, message)
        messageManager.addMessage(session, message)
        messageManager.addMessage(session, message)
        val replica = messageManager.makeReplica(FRAME_TIME)

        assertNotNull("Replica is not null?", replica)
        if (replica != null) {
            assertTrue("The size of replica obj == 1?", replica.data.objects.size == 1)
        }
    }

    @Test
    fun `change coords`() {
        val pawn = Pawn(0)
        pawn.changePosition(Point(32, 32))
        assertEquals(setOf(Point(1, 1)),pawn.coords)

        pawn.changePosition(Point(32 + 16, 32 + 16))
        assertEquals(setOf(Point(1, 1), Point(2, 2), Point(1, 2), Point(2, 1)),pawn.coords)

        pawn.changePosition(Point(32, 32 + 16))
        assertEquals(setOf(Point(1, 1), Point(1, 2)),pawn.coords)

        pawn.changePosition(Point(22, 478))
        assertEquals(setOf(Point(1, 15), Point(2, 1)),pawn.coords)
    }

}