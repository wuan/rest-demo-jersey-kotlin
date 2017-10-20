package com.tngtech.demo.weather.resources.weather

import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.domain.measurement.DataPoint
import com.tngtech.demo.weather.domain.measurement.DataPointType
import com.tngtech.demo.weather.repositories.StationRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.ws.rs.core.Response
import java.util.UUID

import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WeatherResourceIntegrationTest {

    @Autowired
    private lateinit var weatherResource: WeatherResource

    @Autowired
    private lateinit var stationRepository: StationRepository

    private lateinit var dataPoint: DataPoint

    private lateinit var station1: WithId<Station>
    private lateinit var station2: WithId<Station>
    private lateinit var station3: WithId<Station>
    private lateinit var station4: WithId<Station>

    @Before
    @Throws(Exception::class)
    fun setUp() {
        station1 = WithId.create(Station(name = "ABC", latitude = 49.0, longitude = 11.0))
        stationRepository.addStation(station1)
        station2 = WithId.create(Station(name="DEF", latitude=50.0, longitude=10.0))
        stationRepository.addStation(station2)
        station3 = WithId.create(Station(name="GHI", latitude=51.0, longitude=12.0))
        stationRepository.addStation(station3)
        station4 = WithId.create(Station(name="JKL", latitude=55.0, longitude=13.0))
        stationRepository.addStation(station4)

        dataPoint = DataPoint(type=DataPointType.WIND, count=10, first=10, median=20, last=30, mean=22.0)
        weatherResource.updateWeather(station1.id, dataPoint)
        weatherResource.queryWeatherByStation(station1.id, null)
    }

    @Test
    @Throws(Exception::class)
    fun pingShouldReturnDatasizeAndIataFreqInformation() {
        val ping = weatherResource.statistics
        assertThat(ping["datasize"]).isEqualTo(1)
        assertThat((ping["station_freq"] as Map<*, *>).entries).hasSize(4)
    }

    @Test
    @Throws(Exception::class)
    fun weatherQueryShouldReturnPreviouslyUploadedData() {
        val ais = weatherResource.queryWeatherByStation(station1.id, null)
        assertThat(ais[0].wind).isEqualTo(dataPoint)
    }

    @Test
    @Throws(Exception::class)
    fun weatherQueryWithIncludingNearbyRadiusShouldReturnMultipleResults() {
        // check datasize response

        weatherResource.updateWeather(station2.id, dataPoint)
        dataPoint = dataPoint.copy(mean=40.0)
        weatherResource.updateWeather(station3.id, dataPoint)
        dataPoint = dataPoint.copy(mean=30.0)
        weatherResource.updateWeather(station4.id, dataPoint)

        val ais = weatherResource.queryWeatherByStation(station2.id, 300.0)
        assertThat(ais).hasSize(3)
    }

    @Test
    @Throws(Exception::class)
    fun consecutiveWeatherUpdatesOfDifferentTypeShouldBeAccumulated() {
        val windDp = DataPoint(type=DataPointType.WIND, count=10, first=10, median=20, last=30, mean=22.0)
        weatherResource.updateWeather(station1.id, windDp)
        weatherResource.queryWeatherByStation(station1.id, null)

        val ping = weatherResource.statistics
        assertThat(ping["datasize"]).isEqualTo(1)

        val cloudCoverDp = DataPoint(type=DataPointType.CLOUDCOVER, count=4, first=10, median=60, last=100, mean=50.0)
        weatherResource.updateWeather(station1.id, cloudCoverDp)

        val ais = weatherResource.queryWeatherByStation(station1.id, null)
        assertThat(ais[0].wind).isEqualTo(windDp)
        assertThat(ais[0].cloudCover).isEqualTo(cloudCoverDp)
    }

    @Test
    @Throws(Exception::class)
    fun addingWeatherForNonExistentStationReturns406() {
        val stationId = UUID.randomUUID()
        val response = weatherResource.updateWeather(stationId, null)
        assertThat(response.status).isEqualTo(406)
    }

    @Test
    @Throws(Exception::class)
    fun queryWeatherByLocation() {
        val atmosphericDatas = weatherResource.queryWeatherByLocation(WeatherQueryParam(49.0, 11.0, 500.0))

        assertThat(atmosphericDatas).isEmpty()
    }
}