package com.tngtech.demo.weather.resources.weather

import com.tngtech.demo.weather.resources.Paths
import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam

data class WeatherQueryParam(
        @QueryParam(Paths.LATITUDE)
        internal val latitude: Double?,

        @QueryParam(Paths.LONGITUDE)
        internal val longitude: Double?,

        @DefaultValue("250.0")
        @QueryParam(Paths.RADIUS)
        internal val radius: Double
)
