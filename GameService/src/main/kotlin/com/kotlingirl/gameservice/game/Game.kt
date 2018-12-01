package com.kotlingirl.gameservice.game

import com.kotlingirl.serverconfiguration.util.IntIdGen
import com.kotlingirl.serverconfiguration.util.extensions.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.util.IdGenerator

@Component
@Scope("prototype")
class Game(val count: Int) {
    companion object {
        private val log = logger()
        @Autowired
        private lateinit var idGen: IntIdGen
//        todo private lateinit var idGen: IdGenerator
    }
    val id = idGen.getId()

    fun start() {
        log.info("HAHA, game number $id started, congratulations!")
    }
}
