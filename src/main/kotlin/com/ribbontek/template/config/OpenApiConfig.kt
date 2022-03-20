package com.ribbontek.template.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
class OpenApiConfig : WebMvcConfigurer {

    @Value("\${redoc.enabled:true}")
    private var redocEnabled: Boolean? = null

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info().title("Template API").description("A template API")
            )
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        if (redocEnabled == true) {
            registry.addResourceHandler("/redoc")
                .addResourceLocations("classpath:/redoc")
                .setCachePeriod(3000)
                .resourceChain(true)
                .addResolver(object : PathResourceResolver() {
                    override fun getResource(resourcePath: String, location: Resource): Resource =
                        ClassPathResource("/redoc/index.html")
                })
        }
    }
}
