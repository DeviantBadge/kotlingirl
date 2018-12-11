package com.kotlingirl.serverconfiguration.elements.matchmaker

import org.springframework.cloud.client.ServiceInstance

// todo add ServerInstance
data class MatchMakerGameUnit(
        val serviceInstance: ServiceInstance,
        val id: Int,
        val maxPlayers: Int = 3,
        var currentPlayers: Int = 0) {
    val ready: Boolean
        get() = currentPlayers >= maxPlayers

    fun incPlayers(){
        currentPlayers++
    }
}