package com.tngtech.demo.weather.repositories

import com.tngtech.demo.weather.domain.measurement.DataPoint
import com.tngtech.demo.weather.domain.measurement.DataPointType
import com.tngtech.demo.weather.lib.TimestampFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StationDataRepositoryTest {

    @Mock
    private lateinit var timestampFactory: TimestampFactory

    @InjectMocks
    private lateinit var repository: StationDataRepository

    @Test
    fun shouldAcceptWindDataInValidRange() {
        repository.update(createData(DataPointType.WIND, 0.0))

        assertThat(repository.toData().wind).isEqualTo(createData(DataPointType.WIND, 0.0))
    }

    @Test
    fun shouldIgnoreWindDataInValidRange() {
        repository.update(createData(DataPointType.WIND, -0.0001))

        assertThat(repository.toData().wind).isNull()
    }

    @Test
    fun shouldAcceptTemperatureDataAtLowestValidValue() {
        val data = createData(DataPointType.TEMPERATURE, -50.0)
        repository.update(data)

        assertThat(repository.toData().temperature).isEqualTo(data)
    }

    @Test
    fun shouldAcceptTemperatureDataAtHighestValidValue() {
        val data = createData(DataPointType.TEMPERATURE, 99.999999)
        repository.update(data)

        assertThat(repository.toData().temperature).isEqualTo(data)
    }

    @Test
    fun shouldIgnoreTemperatureDataBelowLowestValidValue() {
        repository.update(createData(DataPointType.TEMPERATURE, -50.0001))

        assertThat(repository.toData().temperature).isNull()
    }

    @Test
    fun shouldIgnoreTemperatureDataAboveHighestValidValue() {
        repository.update(createData(DataPointType.TEMPERATURE, 100.0))

        assertThat(repository.toData().temperature).isNull()
    }

    private fun createData(type: DataPointType, mean: Double): DataPoint {
        return DataPoint(type = type, first = 0, median = 1, last = 0, count = 1, mean = mean)
    }
}