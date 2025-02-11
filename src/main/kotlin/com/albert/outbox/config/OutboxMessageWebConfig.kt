package com.albert.outbox.config

import com.albert.outbox.config.interceptor.OutBoxMessagePublisherInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class OutboxMessageWebConfig(
    private val outBoxMessagePublisherInterceptor: OutBoxMessagePublisherInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(outBoxMessagePublisherInterceptor)
    }
}
