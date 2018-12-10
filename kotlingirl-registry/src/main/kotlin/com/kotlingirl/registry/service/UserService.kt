package com.kotlingirl.registry.service

import com.kotlingirl.registry.model.Player

interface UserService {
    fun checkUser(login: String)

    fun registrateUser(login: String, password: String): Player

    fun login(login: String, password: String): Player
    fun updateUserRating(ratingDelta: Int, playerId: Long): Player
    fun updateUser(player: Player): Player
    fun getAllUsers(): MutableList<Player>
    fun getAllOnlineUser(): List<Player>
}