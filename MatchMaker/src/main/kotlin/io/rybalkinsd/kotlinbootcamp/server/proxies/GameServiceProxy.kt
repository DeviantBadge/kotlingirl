package io.rybalkinsd.kotlinbootcamp.server.proxies

import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.xml.ws.Response

@Service
@FeignClient(name = "GameService")
@RibbonClient(name = "GameService")
@RequestMapping("/game")
interface GameServiceProxy {
    @PostMapping(
            path = ["/create"],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun create(@RequestParam("playerCount") playerCount: Int)
            : String
}