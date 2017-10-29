package com.tngtech.demo.weather

import com.mercateo.common.rest.schemagen.link.LinkFactoryContext
import com.mercateo.common.rest.schemagen.link.LinkFactoryContextDefault
import com.tngtech.demo.weather.resources.config.RestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.net.URI

@Configuration
@Import(RestConfiguration::class)
open class WeatherServerTestConfiguration {
    @Bean
    open fun linkFactoryContext(): LinkFactoryContext {
        return LinkFactoryContextDefault(URI("http://host/path"), { x -> true }) { x, y -> true }
    }
}
