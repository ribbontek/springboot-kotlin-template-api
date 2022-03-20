package com.ribbontek.template.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

@Configuration
class RequestLoggingFilterConfig {

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter? {
        return object : CommonsRequestLoggingFilter() {
            override fun shouldLog(request: HttpServletRequest): Boolean {
                return request.getHeader("user-agent") != "Consul Health Check" && super.shouldLog(request)
            }
        }.apply {
            setIncludeQueryString(true)
            setIncludePayload(true)
            setIncludeHeaders(true)
            setMaxPayloadLength(10000)
        }
    }
}
