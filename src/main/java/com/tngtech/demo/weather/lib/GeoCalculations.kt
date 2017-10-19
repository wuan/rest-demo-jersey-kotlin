package com.tngtech.demo.weather.lib

import com.tngtech.demo.weather.domain.gis.Point
import org.springframework.stereotype.Component

@Component
class GeoCalculations {

    /**
     * Haversine distance between two points.
     *
     * @param point1
     * @param point2
     * @return the distance in KM
     */

    fun calculateDistance(point1: Point, point2: Point): Double {
        val deltaLat = Math.toRadians(point2.latitude - point1.latitude)
        val deltaLon = Math.toRadians(point2.longitude - point1.longitude)
        val a = Math.pow(Math.sin(deltaLat / 2), 2.0) + (Math.pow(Math.sin(deltaLon / 2), 2.0)
                * Math.cos(point1.latitude) * Math.cos(point2.latitude))
        val c = 2 * Math.asin(Math.sqrt(a))
        return EARTH_RADIUS * c
    }

    companion object {
        private val EARTH_RADIUS = 6372.8
    }
}
