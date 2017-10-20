package com.tngtech.demo.weather.domain

import com.tngtech.demo.weather.domain.gis.Point

data class Station(
        val name: String,
        override val latitude: Double,
        override val longitude: Double
) : Point
