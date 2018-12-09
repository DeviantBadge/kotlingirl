package com.kotlingirl.gateway

import com.kotlingirl.serverconfiguration.GlobalConstants
import com.kotlingirl.serverconfiguration.GlobalConstants.GAME_SERVICE_NAME_LOWER
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.EurekaClient
import org.apache.http.client.utils.URIBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class GameServiceFilter : AbstractGatewayFilterFactory<Any>(Any::class.java) {

    @Autowired
    lateinit var eurekaClient: EurekaClient

    override fun apply(config: Any?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val req = exchange.request
            val parameters = req.queryParams.toMutableMap()
            // todo remove debug output
            println(req.uri)
            println(exchange.attributes[GATEWAY_REQUEST_URL_ATTR])

            val instance = getInfo(parameters["server"]?.firstOrNull())
                    ?: return@GatewayFilter chain.filter(exchange)
            parameters.remove("server")

            val newUri = buildNewUri(instance, req.uri, parameters);
            exchange.attributes[GATEWAY_REQUEST_URL_ATTR] = newUri
            println(exchange.attributes[GATEWAY_REQUEST_URL_ATTR])

            val newReq = req.mutate()
                    .uri(newUri)
                    .build()
            val exc = exchange.mutate()
                    .request(newReq)
                    .build()

            println(exc.request.uri)
            println(exc.attributes)

            val route = exchange.getAttribute<Route>(GATEWAY_ROUTE_ATTR)
                    ?: return@GatewayFilter chain.filter(exchange)
            println(route)

            val newRoute = buildNewRoute(route, newUri)
            exchange.attributes[GATEWAY_ROUTE_ATTR] = newRoute

            chain.filter(exchange.mutate().request(newReq).build())
        }
    }

    // todo remove starts with
    fun getInfo(serverName: String?): InstanceInfo? = eurekaClient
            .getInstancesByVipAddress(GAME_SERVICE_NAME_LOWER, false)
            .firstOrNull { it.instanceId.startsWith(serverName ?: "___") }

    fun buildNewUri(instance: InstanceInfo, oldUri: URI, parameters: Map<String?, List<String?>>) =
            UriComponentsBuilder.fromUri(oldUri)
                    .host(instance.hostName)
                    .port(instance.port)
                    .replaceQueryParams(CollectionUtils.toMultiValueMap(parameters))
                    .build().encode()
                    .toUri()
                    .also { println(it) }

    fun buildNewRoute(oldRoute: Route, newUri: URI) = Route.AsyncBuilder()
            .id(oldRoute.id)
            .uri(getUriHead(newUri))
            .order(oldRoute.order)
            .asyncPredicate(oldRoute.predicate)
            .filters(oldRoute.filters)
            .build()!!

    fun getUriHead (uri: URI) = URIBuilder()
            .setScheme(uri.scheme)
            .setHost(uri.host)
            .setPort(uri.port)
            .build()!!

//    fun getUriHead(uri: URI) = uri.

    override fun getConfigClass() = Any::class.java

    override fun newConfig() = Any()
}