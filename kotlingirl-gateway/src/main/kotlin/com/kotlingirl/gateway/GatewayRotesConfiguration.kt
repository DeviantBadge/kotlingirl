package com.kotlingirl.gateway

import com.kotlingirl.serverconfiguration.GlobalConstants.KOTLINGIRL_FRONT_NAME
import com.kotlingirl.serverconfiguration.GlobalConstants.KOTLINGIRL_REGISTRY_NAME
import com.kotlingirl.serverconfiguration.GlobalConstants.MATCHMAKER_NAME
import com.kotlingirl.serverconfiguration.MatchMakerConstants.PLAY_PATH
import com.kotlingirl.serverconfiguration.RegistryConstants.USERS_PATH
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
                    spec.path("$PLAY_PATH/**")
                            .uri("lb://$MATCHMAKER_NAME")
                }
                .route("registry_route") { spec ->
                    spec.path("$USERS_PATH/**")
                            .uri("lb://$KOTLINGIRL_REGISTRY_NAME")

                }
                .route("front_route") { spec ->
                    spec.path("/**").and().method("GET")
                            .uri("lb://$KOTLINGIRL_FRONT_NAME")
                            .order(Ordered.LOWEST_PRECEDENCE)
                }
                // sockjs in properties
                .build()
    }
}