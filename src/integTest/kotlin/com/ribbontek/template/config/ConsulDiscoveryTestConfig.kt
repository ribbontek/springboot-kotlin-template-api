package com.ribbontek.template.config

import com.ribbontek.template.generator.MapAPIClient
import com.ribbontek.template.generator.QuestAPIClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(
    clients = [
        MapAPIClient::class,
        QuestAPIClient::class
    ]
)
class ConsulDiscoveryTestConfig
