package com.kotlingirl.serverconfiguration.proxies.gameservice

import com.kotlingirl.serverconfiguration.GlobalConstants.GAME_SERVICE_NAME
import com.kotlingirl.serverconfiguration.proxies.matchmaker.MatchMakerControllerInterface
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service

// todo can we inherit this class in game service ?
// IMPORTANT
// VERY IMPORTANT !!!!!
// IF YOU WANNA USE FEIGN PROXY YOU MUST ANNOTATE APPLICATION CLASS WITH
// @EnableFeignClients("com.kotlingirl")
// OR
// @EnableFeignClients(basePackageClasses = [ServerConfigurationApplication::class])
@Service
@FeignClient(name = GAME_SERVICE_NAME)
@RibbonClient(name = GAME_SERVICE_NAME)
interface GameServiceProxy: GameServiceControllerInterface
