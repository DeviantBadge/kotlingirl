package com.kotlingirl.registry.controllers

import com.kotlingirl.registry.model.Player
import com.kotlingirl.registry.repositories.PlayerRepository
import com.kotlingirl.registry.service.UserService
import com.kotlingirl.serverconfiguration.RegistryConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(RegistryConstants.USERS_PATH)
class PlayerController {
    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Autowired
    lateinit var userService: UserService

    @PostMapping(
            path = ["/registration"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registration(login: String, password: String): Player {
        userService.checkUser(login)
        return playerRepository.findByLogin(login)
    }

    @GetMapping(
            path = ["/login"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(name: String, password: String): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PatchMapping(
            path = ["/update"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(name: String, password: String): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping(
            path = ["/allUsers"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsers(): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping(
            path = ["/onlineUsers"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getOnlineUsers(): ResponseEntity<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}