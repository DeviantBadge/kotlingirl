package com.kotlingirl.gateway

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered


@Configuration
class GatewayRotesConfiguration {
    @Autowired
    lateinit var gameServiceFilter: GameServiceFilter

    @Bean
    fun customTestLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
                .route("match_maker_route") { spec ->
                    spec.path("/play/**")
                            .uri("lb://kotlingirl-matchmaker")
                }
                .route("registry_route") { spec ->
                    spec.path("/users/**")
                            .uri("lb://kotlingirl-registry")

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