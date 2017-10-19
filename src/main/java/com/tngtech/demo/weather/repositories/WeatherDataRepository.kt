package com.tngtech.demo.weather.repositories

import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.domain.measurement.DataPoint
import io.vavr.control.Option
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Provider

@Component
class WeatherDataRepository @Inject
private constructor(private val stationDataRepositoryProvider: Provider<StationDataRepository>) {

    private val stationDataRepositoryByStationId = ConcurrentHashMap<UUID, StationDataRepository>()

    val weatherData: Sequence<AtmosphericData>
        get() = stationDataRepositoryByStationId.values.asSequence()
                .map { it.toData() }

    /**
     * Update data for station with stationId, creates a new [StationDataRepository] if `stationId` is not known.
     *
     * @param stationId id which is used to reference the station
     * @param data      data to be updated
     */
    fun update(stationId: UUID, data: DataPoint) {
        stationDataRepositoryByStationId
                .computeIfAbsent(stationId) { k -> stationDataRepositoryProvider.get() }
                .update(data)
    }

    fun getWeatherDataFor(stationId: UUID): AtmosphericData? {
        return stationDataRepositoryByStationId[stationId]?.toData()
    }

    fun removeStation(stationId: UUID) {
        stationDataRepositoryByStationId.remove(stationId)
    }
}
