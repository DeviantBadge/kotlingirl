package com.kotlingirl.serverconfiguration.util

import org.springframework.context.annotation.Scope
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

@Service
@Scope("prototype")
// its better to use common Id Generator
class PutBackIntIdGen {
    private val lastReadyId = AtomicInteger(0)
    private val freeIds = ConcurrentLinkedQueue<Int>()

    fun getId() = freeIds.poll()
            ?: lastReadyId.getAndIncrement()

    fun putBack(int: Int) {
        freeIds.add(int)
    }
}