package com.kotlingirl.gateway

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.HIGHEST_PRECEDENCE
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.LOWEST_PRECEDENCE
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER
import org.springframework.core.Ordered
import reactor.core.publisher.toMono


@Configuration
class GatewayRotesConfiguration {
    @Autowired
    lateinit var gameServiceFilter: GameServiceFilter

    @Bean
    fun customTestLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
                .route("match_maker_route") { spec ->
                    spec.path("/play/**")
                            .uri("lb://matchmaker")
                }
                .route("registry_route") { spec ->
                    spec.path("/users/**")
                            .uri("lb://registry")

                }
                .route("front_route") { spec ->
                    spec.path("/**").and().method("GET")
                            .uri("lb://kotlingirl-front")
                            .order(Ordered.LOWEST_PRECEDENCE)
                }
                // sockjs in properties
                .build()
    }
}