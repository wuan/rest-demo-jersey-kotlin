package com.tngtech.demo.weather.resources

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.repositories.StationRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RootResourceIntegrationTest {

    @Autowired
    private lateinit var stationRepository: StationRepository

    @Autowired
    private lateinit var rootResource: RootResource

    @Before
    @Throws(Exception::class)
    fun setUp() {
        stationRepository.addStation(WithId.create(Station(name = "ABC", latitude = 49.0, longitude = 11.0)))
        stationRepository.addStation(WithId.create(Station(name = "DEF", latitude = 49.0, longitude = 11.0)))
        stationRepository.addStation(WithId.create(Station(name = "GHI", latitude = 49.0, longitude = 11.0)))
        stationRepository.addStation(WithId.create(Station(name = "JKL", latitude = 55.0, longitude = 11.0)))
    }

    @Test
    fun callingRootResourceShouldReturnAvailableLinks() {
        val root = rootResource.root

        assertThat(root.schema.getByRel(WeatherRel.STATIONS)).isPresent()
        assertThat(root.schema.getByRel(WeatherRel.QUERY)).isPresent()
        assertThat(root.schema.getByRel(WeatherRel.STATISTICS)).isPresent()
    }

}