package com.tngtech.demo.weather.resources.config

import com.tngtech.demo.weather.resources.RootResource
import com.tngtech.demo.weather.resources.stations.StationsResource
import com.tngtech.demo.weather.resources.weather.WeatherResource
import io.swagger.jaxrs.config.BeanConfig
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.logging.LoggingFeature
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Configuration

import javax.ws.rs.ApplicationPath

@Configuration
@ApplicationPath("/api")
open class JerseyConfiguration : ResourceConfig() {
    init {
        register(JacksonFeature::class.java)
        register(LoggingFeature::class.java)

        // Register resources and providers using package-scanning.
        //packages(RESOURCE_BASE_PACKAGE);
        // There still seem to be problems with nested jars and Jersey, registering resources manually by now
        register(RootResource::class.java)
        register(StationsResource::class.java)
        register(WeatherResource::class.java)

        val beanConfig = BeanConfig()
        beanConfig.version = "1.0.0"
        beanConfig.schemes = arrayOf("http")
        beanConfig.host = "localhost:9090"
        beanConfig.basePath = "/api"
        beanConfig.resourcePackage = RESOURCE_BASE_PACKAGE
        //beanConfig.setScan(true);

        register(ApiListingResource::class.java)
        register(SwaggerSerializers::class.java)
    }

    companion object {
        private val RESOURCE_BASE_PACKAGE = "com.tngtech.demo.weather.resources"
    }
}
