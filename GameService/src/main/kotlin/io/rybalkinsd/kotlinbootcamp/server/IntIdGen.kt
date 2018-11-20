package io.rybalkinsd.kotlinbootcamp.server

import java.util.concurrent.atomic.AtomicInteger

class IntIdGen {
    private val lastReadyId = AtomicInteger(0)

    fun getId() =
            lastReadyId.getAndIncrement()
}