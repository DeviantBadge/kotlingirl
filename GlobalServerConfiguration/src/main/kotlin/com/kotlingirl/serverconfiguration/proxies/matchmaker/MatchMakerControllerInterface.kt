package com.kotlingirl.serverconfiguration.proxies.matchmaker

import com.kotlingirl.serverconfiguration.MatchMakerConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(MatchMakerConstants.PLAY_PATH)
interface MatchMakerControllerInterface {
    @PostMapping(
            path = [MatchMakerConstants.CASUAL_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun casual(@RequestBody body: String): ResponseEntity<String>

    @PostMapping(
            path = [MatchMakerConstants.RANKED_PATH],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun ranked(@RequestBody  body: String): ResponseEntity<String>
}
