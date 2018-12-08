package com.kotlingirl.matchmaker.service

import com.kotlingirl.matchmaker.model.Player
import java.util.*

interface UserService {
    fun checkUser(login: String)

    fun registrateUser(login: String, password: String): Player

    fun login(login: String, password: String): Player
}