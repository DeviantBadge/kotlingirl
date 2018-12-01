package com.kotlingirl.serverconfiguration.proxies

import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Service
@FeignClient(name = "GameService")
@RibbonClient(name = "GameService")
// todo can we inherit this class in game service ?
// IMPORTANT
// VERY IMPORTANT !!!!!
// IF YOU WANNA USE FEIGN PROXY YOU MUST ANNOTATE APPLICATION CLASS WITH
// @EnableFeignClients("com.kotlingirl")
interface GameServiceProxy: GameServiceControllerInterface
