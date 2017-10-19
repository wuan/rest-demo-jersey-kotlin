package com.tngtech.demo.weather.domain.gis

data class Location(
        override val latitude: Double,
        override val longitude: Double
) : Point
