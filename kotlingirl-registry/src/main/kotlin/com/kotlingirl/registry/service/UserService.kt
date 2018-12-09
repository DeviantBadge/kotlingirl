package com.kotlingirl.registry.service

import com.kotlingirl.registry.model.Player

interface UserService {
    fun checkUser(login: String)

    fun registrateUser(login: String, password: String): Player

    fun login(login: String, password: String): Player
}