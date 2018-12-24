package com.kotlingirl.registry.service

import com.kotlingirl.registry.model.Player
import com.kotlingirl.registry.repositories.PlayerRepository
import com.kotlingirl.serverconfiguration.elements.InternalException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.codec.Hex.encode
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    override fun login(login: String, password: String): Player {
        val player: Player = playerRepository.findByLogin(login) ?: throw InternalException(HttpStatus.BAD_REQUEST, "Wrong login or password")
        if ((player.password.contentEquals(encode((password + "Nikolay").toByteArray()))) /*&& !player.logged*/) {
            player.logged = true
            return playerRepository.save(player)
        }
        else throw InternalException(HttpStatus.BAD_REQUEST, "bad credentional")

    }

    override fun registrateUser(login: String, password: String): Player {
        checkUser(login)
        var player: Player? = playerRepository.findByLogin(login)
        if(player != null) throw InternalException(HttpStatus.BAD_REQUEST, "This name already registrated")
        return playerRepository.save(Player(login, encode((password + "Nikolay").toByteArray())))
    }

    override fun checkUser(login: String): Unit = when {
        login.length < 3 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too short name")
        login.length > 20 -> throw InternalException(HttpStatus.BAD_REQUEST, "Too Long Name")
        else -> {
        }
    }

    override fun updateUser(player: Player) = playerRepository.save(player)

    override fun getUser(playerId: Long): Player {
        return playerRepository.findById(playerId).orElse(null)
                ?: throw InternalException(HttpStatus.BAD_REQUEST, "Player with this id isnt create")
    }

    override fun updateUserRating(ratingDelta: Int, playerId: Long): Player {
        var player: Player = playerRepository.findById(playerId).orElse(null)
                ?: throw InternalException(HttpStatus.BAD_REQUEST, "Player with this id isnt create")
        player.rating += ratingDelta
        player.gamesPlayed ++
        if(ratingDelta > 0) player.gamesWon++
        if(player.rating  < 0) player.rating = 0
        return playerRepository.save(player)
    }

    override fun setUserRating(newRating: Int, playerId: Long): Player {

        var player: Player = playerRepository.findById(playerId).orElse(null)
                ?: throw InternalException(HttpStatus.BAD_REQUEST, "Player with this id isnt create")
        player.rating = newRating
        if(player.rating  < 0) player.rating = 0
        return playerRepository.save(player)
    }

    override fun getAllUsers() = playerRepository.findAll()

    override fun getAllOnlineUser() = playerRepository.findAll().filter{o -> o.logged}
}