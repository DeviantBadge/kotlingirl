package com.kotlingirl.serverconfiguration.proxies.matchmaker

import com.kotlingirl.serverconfiguration.GlobalConstants
import com.kotlingirl.serverconfiguration.GlobalConstants.MATCH_MAKER_NAME
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
@FeignClient(name = MATCH_MAKER_NAME)
@RibbonClient(name = MATCH_MAKER_NAME)
interface MatchMakerProxy: MatchMakerControllerInterface

// this proxies works quite unpredictable, they create rest to the whole service, instead of interna api as i wanted
// todo how to create internal proxy with spring (that easy to use and handle)