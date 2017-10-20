package com.tngtech.demo.weather.lib

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test


class GeoCalculationsTest {

    private val geoCalculations = GeoCalculations()

    @Test
    fun calculateDistanceHalfwayAroundTheEquatorShouldYieldHalveEarthCircumference() {
        val point1 = Point(0.0, 0.0)
        val point2 = Point(0.0, 180.0)

        assertThat(geoCalculations.calculateDistance(point1, point2)).isEqualTo(20020.741, Offset.offset(1e-3))
        assertThat(geoCalculations.calculateDistance(point2, point1)).isEqualTo(20020.741, Offset.offset(1e-3))
    }

    @Test
    fun calculateDistanceFromTheEquatorToTheNorthernTropicShouldYieldRoughly2600Kilometers() {
        val point1 = Point(0.0, 11.0)
        val point2 = Point(23.5, 11.0)

        assertThat(geoCalculations.calculateDistance(point1, point2)).isEqualTo(2613.819, Offset.offset(1e-3))
        assertThat(geoCalculations.calculateDistance(point2, point1)).isEqualTo(2613.819, Offset.offset(1e-3))
    }

    internal data class Point(override val latitude: Double, override val longitude: Double) : com.tngtech.demo.weather.domain.gis.Point
}