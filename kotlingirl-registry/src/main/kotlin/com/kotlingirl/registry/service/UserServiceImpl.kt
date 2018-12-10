package com.kotlingirl.registry.service

import com.kotlingirl.registry.model.Player
import com.kotlingirl.registry.repositories.PlayerRepository
import com.kotlingirl.serverconfiguration.elements.InternalException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    override fun login(login: String, password: String): Player {
        val player: Player = playerRepository.findByLogin(login)
        if (player.password == password) return player
        else throw InternalException(HttpStatus.BAD_REQUEST, "bad credentional")

    }

    override fun registrateUser(login: String, password: String): Player {
        val player: Player? = playerRepository.findByLogin(login)
        if(player != null) throw InternalException(HttpStatus.BAD_REQUEST, "This name already registrated")
        return playerRepository.save(Player(login, password))
    }

    override fun checkUser(login: String): Unit = when {
        login.length < 3 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too short name")
        login.length > 20 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too Long Name")
        else -> {
        }
    }

}