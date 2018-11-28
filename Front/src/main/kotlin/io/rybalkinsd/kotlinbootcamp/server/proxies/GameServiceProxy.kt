package io.rybalkinsd.kotlinbootcamp.server.proxies

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
        name = "GameService",
        url = "localhost:8090")
@Service
interface GameServiceProxy {
    @PostMapping(
            path = ["/create"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun create(@RequestParam("playerCount") playerCount: Int);
}