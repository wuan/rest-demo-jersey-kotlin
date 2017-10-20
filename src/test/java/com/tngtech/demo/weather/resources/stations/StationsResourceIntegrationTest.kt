package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.PaginatedResponse
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.repositories.StationRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StationsResourceIntegrationTest {

    @Autowired
    private lateinit var stationsResource: StationsResource

    @Autowired
    private lateinit var stationRepository: StationRepository

    @Test
    fun getStationsShouldReturnListOfStations() {
        stationRepository.addStation(WithId.create(Station(name="FOO", latitude=49.0, longitude=11.0)))
        val response = stationsResource.getStations(0, 1000)

        assertThat(response.`object`).isNotNull()

        assertThat(response.`object`.members).extracting("object").extracting("object").extracting("name").contains("FOO")
    }

    @Test
    fun gettingExistingStationReturnsData() {
        val newStation = stationsResource.addStation(Station(name="FOO", latitude=49.0, longitude=11.0))

        val station = stationRepository.getStationById(newStation.`object`.id)?.`object`
        assertThat(station).isNotNull()

        assertThat(station?.name).isEqualTo("FOO")
        assertThat(station?.latitude).isEqualTo(49.0)
        assertThat(station?.longitude).isEqualTo(11)
    }

    @Test
    fun getSubresource() {
        val stationResourceClass = stationsResource.stationSubResource()

        assertThat(stationResourceClass).isNotNull()
    }

}