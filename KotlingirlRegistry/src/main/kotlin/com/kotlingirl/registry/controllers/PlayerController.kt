package com.kotlingirl.registry.controllers

import com.kotlingirl.registry.model.Player
import com.kotlingirl.registry.repositories.PlayerRepository
import com.kotlingirl.registry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class PlayerController: UserControllerInterface {
    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Autowired
    lateinit var userService: UserService

    override fun registration(login: String, password: String): Player {
        userService.checkUser(login)
        return playerRepository.findByLogin(login)
    }

    override fun login(name: String, password: String): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(name: String, password: String): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllUsers(): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOnlineUsers(): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}