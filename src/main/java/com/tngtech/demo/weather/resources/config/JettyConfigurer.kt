package com.tngtech.demo.weather.resources.config

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.util.resource.ResourceCollection
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer

class JettyConfigurer : JettyServerCustomizer {

    override fun customize(server: Server) {
        val webAppContext = server.handler as WebAppContext
        try {
            Resource.newClassPathResource("/static").use { staticResource ->
                Resource.newClassPathResource("/META-INF/resources/webjars/").use { webjarsResource ->
                    webAppContext.contextPath = "/"
                    webAppContext.baseResource = ResourceCollection( //
                            staticResource, //
                            webjarsResource //
                    )
                }
            }
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }

    }
}