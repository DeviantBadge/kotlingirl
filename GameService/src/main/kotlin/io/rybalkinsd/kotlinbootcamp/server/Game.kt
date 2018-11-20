package io.rybalkinsd.kotlinbootcamp.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class Game(val count: Int) {
    companion object {
        private val log = logger()
        private val idGen = IntIdGen()
    }
    val id = idGen.getId()

    fun start() {
        log.info("HAHA, game number $id started, congratulations!")
    }
}
