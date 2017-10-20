package com.tngtech.demo.weather.resources.config

import com.mercateo.common.rest.schemagen.link.LinkFactory
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory
import com.mercateo.common.rest.schemagen.link.Scope
import com.mercateo.common.rest.schemagen.link.helper.MethodInvocation
import com.mercateo.common.rest.schemagen.parameter.CallContext
import com.mercateo.common.rest.schemagen.plugin.FieldCheckerForSchema
import com.mercateo.common.rest.schemagen.plugin.MethodCheckerForLink
import com.mercateo.rest.schemagen.spring.JerseyHateoasConfiguration
import com.tngtech.demo.weather.resources.stations.StationResource
import com.tngtech.demo.weather.resources.stations.StationsResource
import com.tngtech.demo.weather.resources.weather.WeatherResource
import lombok.extern.slf4j.Slf4j
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.lang.reflect.Field

import javax.inject.Named

@Configuration
@Slf4j
@Import(JerseyHateoasConfiguration::class)
open class RestConfiguration {

    @Bean
    open fun fieldCheckerForSchema(): FieldCheckerForSchema {
        return object : FieldCheckerForSchema {
            override fun test(t: Field?, u: CallContext?): Boolean {
                return true
            }
        }
    }

    @Bean
    open fun methodCheckerForLink(): MethodCheckerForLink {
        return object : MethodCheckerForLink {
            override fun test(t: Scope?): Boolean {
                return true
            }
        }
    }

    @Bean
    @Named("stationsLinkFactory")
    open fun stationsResourceLinkFactory(linkMetaFactory: LinkMetaFactory): LinkFactory<StationsResource> {
        return linkMetaFactory.createFactoryFor(StationsResource::class.java)
    }

    @Bean
    @Named("stationLinkFactory")
    open fun stationResourceLinkFactory(
            stationsResourceLinkFactory: LinkFactory<StationsResource>): LinkFactory<StationResource> {
        return stationsResourceLinkFactory.subResource({ it.stationSubResource() },
                StationResource::class.java)
    }

    @Bean
    @Named("weatherLinkFactory")
    open fun weatherResourceLinkFactory(linkMetaFactory: LinkMetaFactory): LinkFactory<WeatherResource> {
        return linkMetaFactory.createFactoryFor(WeatherResource::class.java)
    }

    @Bean
    open fun containerFactory(): EmbeddedServletContainerFactory {
        val jettyEmbeddedServletContainerFactory = JettyEmbeddedServletContainerFactory()
        jettyEmbeddedServletContainerFactory.addServerCustomizers(JettyConfigurer())
        return jettyEmbeddedServletContainerFactory
    }
}
