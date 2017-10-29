package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.repositories.StationRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.inject.Inject
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response
import java.util.UUID

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StationResourceIntegrationTest {

    @Inject
    private val stationResource: StationResource? = null

    @Inject
    private val stationRepository: StationRepository? = null

    @Test
    @Throws(Exception::class)
    fun gettigNonExistentStationReturns404() {
        val stationId = UUID.randomUUID()
        assertThatThrownBy { stationResource!!.getStation(stationId) }.isInstanceOf(NotFoundException::class.java).hasMessage("Station with id $stationId was not found")
    }

    @Test
    fun gettingExistingStationReturnsData() {
        val newStation = WithId.create(Station(name="FOO", latitude=49.0, longitude=11.0))
        stationRepository!!.addStation(newStation)

        val response = stationResource!!.getStation(newStation.id)
        val station = response.`object`.`object`

        assertThat(station).isNotNull()

        assertThat(station.name).isEqualTo("FOO")
        assertThat(station.latitude).isEqualTo(49.0)
        assertThat(station.longitude).isEqualTo(11.0)
    }

    @Test
    fun removingExistingStationShouldWork() {
        val newStation = WithId.create(Station(name="FOO", latitude=49.0, longitude=11.0))
        stationRepository!!.addStation(newStation)

        val response = stationResource!!.deleteStation(newStation.id)

        assertThat(response.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)

        assertThatThrownBy { stationResource.getStation(newStation.id) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun removingNonExistingStationShouldWork() {
        val nonExistentStationId = UUID.randomUUID()
        val response = stationResource!!.deleteStation(nonExistentStationId)

        assertThat(response.status).isEqualTo(Response.Status.NO_CONTENT.statusCode)

        assertThatThrownBy { stationResource.getStation(nonExistentStationId) }.isInstanceOf(NotFoundException::class.java)
    }

}