package com.kotlingirl.serverconfiguration.proxies.matchmaker

import com.kotlingirl.serverconfiguration.MatchMakerConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(MatchMakerConstants.USER_PATH)
interface UserControllerInterface {
    @PostMapping(
            path = ["/registration"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registration(@RequestBody login: String, @RequestBody password: String): com.kotlingirl.matchmaker.model.Player

    @GetMapping(
            path = ["/login"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody name: String, @RequestBody password: String): ResponseEntity<String>

    @PatchMapping(
            path = ["/update"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody name: String, @RequestBody password: String): ResponseEntity<String>

    @GetMapping(
            path = ["/allUsers"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsers(): ResponseEntity<String>

    @GetMapping(
            path = ["/onlineUsers"],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getOnlineUsers(): ResponseEntity<String>

}
