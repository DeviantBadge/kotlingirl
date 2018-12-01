package com.kotlingirl.serverconfiguration.elements.matchmaker

import com.sun.org.apache.xpath.internal.operations.Bool
import org.springframework.cloud.client.ServiceInstance

// todo add ServerInstance
data class MatchMakerGameUnit(
        val serviceInstance: ServiceInstance,
        val id: Int,
        val maxPlayers: Int = 2,
        var ready: Boolean = false)