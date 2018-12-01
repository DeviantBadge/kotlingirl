package com.kotlingirl.serverconfiguration.loadbalance

import com.netflix.client.config.IClientConfig
import org.springframework.beans.factory.annotation.Autowired
import com.netflix.loadbalancer.WeightedResponseTimeRule
import com.netflix.loadbalancer.IRule
import com.netflix.loadbalancer.PingUrl
import com.netflix.loadbalancer.IPing
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class RibbonConfiguration {
    @Autowired
    private lateinit var clientConfig: IClientConfig

    @Bean
    fun ribbonPing(config: IClientConfig): IPing {
        return PingUrl()
    }

    @Bean
    fun ribbonRule(config: IClientConfig): IRule {
        return WeightedResponseTimeRule()
    }
}