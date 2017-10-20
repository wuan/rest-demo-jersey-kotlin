package com.tngtech.demo.weather.resources.weather

import com.mercateo.common.rest.schemagen.link.LinkFactory
import com.mercateo.common.rest.schemagen.parameter.CallContext
import com.mercateo.common.rest.schemagen.util.OptionalUtil.collect
import com.tngtech.demo.weather.forCall
import com.tngtech.demo.weather.resources.WeatherRel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.inject.Named
import javax.ws.rs.core.Link

@Component
class WeatherLinkCreator @Autowired
constructor(@param:Named("weatherLinkFactory") private val weatherLinkFactory: LinkFactory<WeatherResource>) {

    fun create(): List<Link> {
        val callContext = CallContext.create()
        val weatherQueryParam = callContext.builderFor(WeatherQueryParam::class.java).defaultValue(WeatherQueryParam(null, null, 250.0)).build()

        return collect(
                weatherLinkFactory.forCall(WeatherRel.STATISTICS, { it.statistics }),
                weatherLinkFactory.forCall(WeatherRel.QUERY) { r -> r.queryWeatherByLocation(weatherQueryParam.get()) }
        )
    }

    fun createForStation(stationId: UUID): List<Link> {
        return collect(
                weatherLinkFactory.forCall(WeatherRel.QUERY) { r -> r.queryWeatherByStation(stationId, null) },
                weatherLinkFactory.forCall(WeatherRel.UPDATE) { r -> r.updateWeather(stationId, null) }
        )
    }
}
