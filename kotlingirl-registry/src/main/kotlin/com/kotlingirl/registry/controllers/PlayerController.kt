package com.kotlingirl.registry.controllers

import com.kotlingirl.registry.model.Player
import com.kotlingirl.registry.service.UserService
import com.kotlingirl.serverconfiguration.RegistryConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import sun.security.util.Password

@RestController
@RequestMapping(RegistryConstants.USERS_PATH)
class PlayerController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping(
            path = ["/registration"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registration(@RequestBody credentionalDTO: CredentionalDTO): Player {
        return userService.registrateUser(credentionalDTO.login, credentionalDTO.password)
    }

    @GetMapping(path = ["/login/{login}/{password}"])
    fun login(@PathVariable login: String, @PathVariable password: String): Player {
        return userService.login(login, password)
    }

    @PatchMapping(path = ["/update"])
    fun update(player: Player): Player {
        return userService.updateUser(player)

    }

    @GetMapping(path = ["/allUsers"])
    fun getAllUsers(): MutableList<Player> {
        return userService.getAllUsers()
    }

/*    @GetMapping(
            path = ["/onlineUsers"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getOnlineUsers(): List<Player> {
        return userService.getAllOnlineUser()
    }*/
}