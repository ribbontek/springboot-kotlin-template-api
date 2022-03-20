package com.ribbontek.template.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(
    basePackages = [
        "com.ribbontek.template.repository.model",
        "com.ribbontek.template.repository.event"
    ]
)
@EnableJpaRepositories(basePackages = ["com.ribbontek.template.repository"])
class ApplicationConfig
