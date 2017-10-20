package com.tngtech.demo.weather.repositories

import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.domain.measurement.DataPoint
import com.tngtech.demo.weather.domain.measurement.DataPointType
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.runners.MockitoJUnitRunner

import javax.inject.Provider
import java.util.UUID

import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class WeatherDataRepositoryTest {

    @Mock
    private lateinit var stationDataRepository: StationDataRepository

    @Spy
    private var dataRepositoryProvider = Provider<StationDataRepository> { stationDataRepository }

    @InjectMocks
    private lateinit var weatherDataRepository: WeatherDataRepository

    @Test
    fun returnsEmptyWeatherDataAfterInitialization() {
        assertThat(weatherDataRepository.weatherData.toList()).isEmpty()
    }

    @Test
    fun returnsEmptyElementAfterInitialization() {
        assertThat(weatherDataRepository.getWeatherDataFor(UUID.randomUUID())).isNull()
    }

    @Test
    fun shouldCreateDataRepositoryAndAddNewDataWhenUpdating() {
        val stationId = UUID.randomUUID()
        val dataPoint = mock(DataPoint::class.java)

        weatherDataRepository.update(stationId, dataPoint)

        assertThat(weatherDataRepository.weatherData.iterator()).hasSize(1)
        verify(stationDataRepository).update(dataPoint)
    }

    @Test
    fun shouldReuseDataRepositoryAndAddNewDataWhenUpdatingAnExistingRepository() {
        val stationId = UUID.randomUUID()
        val dataPoint1 = DataPoint(type=DataPointType.WIND, mean=5.5)
        weatherDataRepository.update(stationId, dataPoint1)

        val dataPoint2 = DataPoint(type=DataPointType.HUMIDITY, mean=25.5)
        weatherDataRepository.update(stationId, dataPoint2)

        assertThat(weatherDataRepository.weatherData.iterator()).hasSize(1)
        verify(dataRepositoryProvider).get()
        verify<StationDataRepository>(stationDataRepository).update(dataPoint1)
        verify<StationDataRepository>(stationDataRepository).update(dataPoint2)
    }

    @Test
    fun getWeatherDataForReturnsDataIfStationIsKnown() {
        val stationId = UUID.randomUUID()
        val dataPoint = mock(DataPoint::class.java)
        weatherDataRepository.update(stationId, dataPoint)
        val atmosphericData = mock(AtmosphericData::class.java)
        `when`(stationDataRepository.toData()).thenReturn(atmosphericData)

        val result = weatherDataRepository.getWeatherDataFor(stationId)

        assertThat(result).isEqualTo(atmosphericData)
    }

    @Test
    fun removeStationIgnoresIfStationDoesNotExist() {
        val stationId = UUID.randomUUID()
        weatherDataRepository.removeStation(stationId)

        assertThat(weatherDataRepository.weatherData.toList()).isEmpty()
    }

    @Test
    fun removeStationRemovesExistingStationRepository() {
        val stationId = UUID.randomUUID()
        val dataPoint = mock(DataPoint::class.java)
        weatherDataRepository.update(stationId, dataPoint)

        weatherDataRepository.removeStation(stationId)

        assertThat(weatherDataRepository.weatherData.toList()).isEmpty()
    }
}