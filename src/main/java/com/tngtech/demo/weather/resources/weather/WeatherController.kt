package com.tngtech.demo.weather.resources.weather

import com.tngtech.demo.weather.domain.gis.Point
import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.lib.EventCounter
import com.tngtech.demo.weather.lib.GeoCalculations
import com.tngtech.demo.weather.lib.TimestampFactory
import com.tngtech.demo.weather.repositories.StationRepository
import com.tngtech.demo.weather.repositories.WeatherDataRepository
import org.springframework.stereotype.Component
import java.util.*
import javax.inject.Inject

fun <T, U> T?.flatMap(body: (T) -> U?): U? =
        if (this != null) body(this) else null

@Component
internal class WeatherController @Inject
constructor(private val stationRepository: StationRepository, private val weatherDataRepository: WeatherDataRepository, private val timestampFactory: TimestampFactory, private val requestFrequency: EventCounter<UUID>, private val radiusFrequency: EventCounter<Double>, private val geoCalculations: GeoCalculations) {

    val statistics: Map<String, Any>
        get() {
            val result = HashMap<String, Any>()

            result.put("datasize", countOfDataUpdatedSinceADayAgo)

            result.put("station_freq", stationFractions)

            result.put("radius_freq", radiusHistogram)

            return result
        }

    private val countOfDataUpdatedSinceADayAgo: Int
        get() {
            val oneDayAgo = timestampFactory.currentTimestamp - MILLISECONDS_PER_DAY

            return weatherDataRepository
                    .weatherData
                    .count { data -> data.lastUpdateTime > oneDayAgo }
        }

    private val stationFractions: Map<UUID, Double>
        get() {
            val freq = HashMap<UUID, Double>()
            stationRepository.stations.map { station -> station.id }.forEach { stationId -> freq.put(stationId, requestFrequency.fractionOf(stationId)) }
            return freq
        }

    private val radiusHistogram: Array<Int>
        get() {
            val maxRange = radiusFrequency.events.max() ?: 1000.0
            val binSize = 10

            val binCount = Math.ceil(maxRange / binSize + 1).toInt()

            val hist = (1..binCount).map { 0 }.toTypedArray()

            radiusFrequency
                    .stream()
                    .forEach { tuple ->
                        val radius = tuple._1()
                        val binIndex = radius!!.toInt() / 10

                        val radiusFrequency = tuple._2().get()
                        hist[binIndex] += radiusFrequency
                    }

            return hist
        }

    /**
     * Retrieve the most up to date atmospheric information from the given stationId and other airports in the given
     * radius.
     *
     * @param stationId the three letter stationId code
     * @param radius    the radius, in km, from which to collect weather data
     * @return an HTTP Response and a list of [AtmosphericData] from the requested stationId and
     * airports in the given radius
     */
    fun queryWeather(stationId: UUID, radius: Double?): List<AtmosphericData> {

        updateRequestFrequency(stationId, radius)

        return if (radius == 0.0 || radius == null) {
            weatherDataRepository
                    .getWeatherDataFor(stationId)?.let { listOf(it) } ?: emptyList()
        } else {
            stationRepository
                    .getStationById(stationId)
                    ?.let { centerStation ->
                        val map: List<UUID> = stationRepository
                                .stations
                                .filter { otherStation ->
                                    geoCalculations
                                            .calculateDistance(otherStation.`object`, centerStation.`object`) <= radius
                                }
                                .map { station -> station.id }
                        map.flatMap { weatherDataRepository.getWeatherDataFor(it) }
                    }
                    ?: emptyList()
        }
    }

    fun queryWeather(location: Point, radius: Double?): List<AtmosphericData> {
        return List.empty()
    }

    fun assertLaw3() {
        val monadValue : Int? = 23
        val lhs = monadValue.flatMap {it}
    }


    private fun updateRequestFrequency(stationId: UUID, radius: Double?) {
        stationRepository
                .getStationById(stationId)
                .forEach { station ->
                    requestFrequency.increment(station.id)
                    radiusFrequency.increment(radius)
                }
    }

    companion object {

        private val MILLISECONDS_PER_DAY = 86400000
    }
}
