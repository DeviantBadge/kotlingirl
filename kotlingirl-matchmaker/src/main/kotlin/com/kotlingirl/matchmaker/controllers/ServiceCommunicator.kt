package com.kotlingirl.matchmaker.controllers

import com.kotlingirl.serverconfiguration.GameServiceConstants.APPEND_PLAYER_PATH
import com.kotlingirl.serverconfiguration.GameServiceConstants.CREATE_PATH
import com.kotlingirl.serverconfiguration.GameServiceConstants.GAME_PATH
import com.kotlingirl.serverconfiguration.GlobalConstants.GAME_SERVICE_NAME
import com.kotlingirl.serverconfiguration.GlobalConstants.GAME_SERVICE_NAME_LOWER
import com.kotlingirl.serverconfiguration.elements.InstanceInfoWrapper
import com.kotlingirl.serverconfiguration.elements.InternalException
import com.kotlingirl.serverconfiguration.elements.matchmaker.MatchMakerGameUnit
import com.kotlingirl.serverconfiguration.elements.messages.GameServiceResponse
import com.kotlingirl.serverconfiguration.elements.messages.UserCredentials
import com.kotlingirl.serverconfiguration.elements.messages.UserRequestParameters
import com.kotlingirl.serverconfiguration.util.extensions.fromJsonString
import com.kotlingirl.serverconfiguration.util.extensions.logger
import com.kotlingirl.serverconfiguration.util.extensions.toJsonHttpEntity
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ServiceCommunicator {
    val log = logger()

    @Autowired
    lateinit var eurekaClient: EurekaClient

    @Autowired
    @Qualifier("no_exc")
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var loadBalancer: LoadBalancerClient

    fun createCasualGame(parameters: UserRequestParameters?): MatchMakerGameUnit {
        val serviceInstance: ServiceInstance
        val response: ResponseEntity<String>
        eurekaClient.getInstancesByVipAddress(GAME_SERVICE_NAME_LOWER, false)
                .forEach { log.error(it.instanceId) }
        try {
            serviceInstance = getService(GAME_SERVICE_NAME_LOWER)
            log.error(serviceInstance.instanceId)
            response = restTemplate.postForEntity(
                    "${serviceInstance.uri}$GAME_PATH$CREATE_PATH",
                    (parameters ?: UserRequestParameters()).toJsonHttpEntity(),
                    String::class.java)

            log.info("Got response from $GAME_SERVICE_NAME with " +
                    "body ${response.body ?: "Empty body"} and " +
                    "response code ${response.statusCode}")
        } catch (e: IllegalStateException) {
            log.info("${e::class.java.simpleName} and message ${e.message}")
            throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Cant choose service to use")
        }
        if (!response.statusCode.is2xxSuccessful)
            throw InternalException(response.statusCode, "Request to $GAME_SERVICE_NAME " +
                    "Ended with status ${response.statusCode} and message: ${response.body ?: "response was empty"}")

        return MatchMakerGameUnit(
                serviceInstance,
                response.body?.fromJsonString(GameServiceResponse::class.java)?.id
                        ?: throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Response with empty body"))
    }

    fun appendPlayerToGame(game: MatchMakerGameUnit, credentials: UserCredentials) {
        val response: ResponseEntity<String>
        try {
            response = restTemplate.postForEntity(
                    "${game.serviceInstance.uri}$GAME_PATH$APPEND_PLAYER_PATH",
                    credentials.toJsonHttpEntity(),
                    String::class.java)

            log.info("Got response from $GAME_SERVICE_NAME with " +
                    "body ${response.body ?: "Empty body"} and " +
                    "response code ${response.statusCode}")
        } catch (e: IllegalStateException) {
            log.info("${e::class.java.simpleName} and message ${e.message}")
            throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Cant choose service to use")
        }
        if (!response.statusCode.is2xxSuccessful)
            throw InternalException(response.statusCode, "Request to $GAME_SERVICE_NAME " +
                    "Ended with status ${response.statusCode} and message: ${response.body ?: "response was empty"}")
    }

    fun getService(serviceName: String): ServiceInstance {
        val serviceInstance = (loadBalancer.choose(GAME_SERVICE_NAME_LOWER)
                ?: throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Cant choose service to use")) as? RibbonLoadBalancerClient.RibbonServer
                ?: throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Server use wrong loader, connect to developers")
        val instanceId = serviceInstance.server?.metaInfo?.instanceId
                ?: throw InternalException(HttpStatus.INTERNAL_SERVER_ERROR, "Cant fetch server instance")
        return InstanceInfoWrapper(instanceId, serviceInstance)
    }
}
