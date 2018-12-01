package com.kotlingirl.serverconfiguration.util

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
@Scope("prototype")
class IntIdGen {
    private val lastReadyId = AtomicInteger(0)

    fun getId() =
            lastReadyId.getAndIncrement()
}