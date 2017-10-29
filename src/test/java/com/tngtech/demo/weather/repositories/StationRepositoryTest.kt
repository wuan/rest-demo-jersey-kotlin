package com.tngtech.demo.weather.repositories

import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class StationRepositoryTest {

    @InjectMocks
    private lateinit var repository: StationRepository

    @Test
    fun shouldBeEmptyAfterInitialization() {
        assertThat(repository.stations.toList()).isEmpty()
    }

    @Test
    fun shouldReturnEmptyResultIfStationIsNotFound() {
        assertThat(repository.getStationById(UUID.randomUUID())).isNull()
    }

    @Test
    fun anAddedStationShouldBePersisted() {
        val stationWithId = WithId.create(Station(name = "foo", latitude = 49.0, longitude = 11.0))
        repository.addStation(stationWithId)

        assertThat(repository.getStationById(stationWithId.id)).has(object : Condition<WithId<Station>?>() {
            override fun matches(station: WithId<Station>?): Boolean {
                return station != null && station.`object`.name == "foo" &&
                        station.`object`.latitude == 49.0 &&
                        station.`object`.longitude == 11.0
            }
        })
    }

    @Test
    fun removingANonExistentStationShouldBeIgnored() {
        repository.removeStation(UUID.randomUUID())

        assertThat(repository.stations.toList()).isEmpty()
    }

    @Test
    fun removedStationShouldDisappearFromRepository() {
        val stationWithId = WithId.create(Station(name = "foo", latitude = 49.0, longitude = 11.0))
        repository.addStation(stationWithId)

        repository.removeStation(stationWithId.id)

        assertThat(repository.getStationById(stationWithId.id)).isNull()
    }

}