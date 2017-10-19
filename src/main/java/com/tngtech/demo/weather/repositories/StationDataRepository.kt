package com.tngtech.demo.weather.repositories

import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.domain.measurement.DataPoint
import com.tngtech.demo.weather.domain.measurement.DataPointType
import com.tngtech.demo.weather.lib.TimestampFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
internal class StationDataRepository @Inject
constructor(private val timestampFactory: TimestampFactory) {

    private val dataPointByType = ConcurrentHashMap<DataPointType, DataPoint>()

    private var lastUpdateTime = 0L

    fun update(data: DataPoint) {
        val shouldAddData = acceptanceRuleByType
                .get(data.type)
                ?.let { rulePredicate: (DataPoint) -> Boolean -> rulePredicate.invoke(data) }
                ?: true

        if (shouldAddData) {
            dataPointByType.put(data.type, data)
            lastUpdateTime = timestampFactory.currentTimestamp
        }
    }

    fun toData(): AtmosphericData {
        val data = AtmosphericData(lastUpdateTime = lastUpdateTime)

        typesWithBuilderMethods.forEach { dataTypeAndBuilderMethod ->
            val dataType = dataTypeAndBuilderMethod.first
            val builderAdapter = dataTypeAndBuilderMethod.second

            val dataPoint: DataPoint? = dataPointByType[dataType]
            dataPoint?.let { builderAdapter.invoke(data, it) }
        }


        return data
    }

    companion object {
        private val acceptanceRuleByType: Map<DataPointType, (DataPoint) -> Boolean> = mapOf(
                Pair(DataPointType.WIND, { dataPoint -> dataPoint.mean >= 0.0 }),
                Pair(DataPointType.TEMPERATURE, { dataPoint -> dataPoint.mean >= -50.0 && dataPoint.mean < 100.0 }),
                Pair(DataPointType.HUMIDITY, { dataPoint -> dataPoint.mean >= 0.0 && dataPoint.mean < 100.0 }),
                Pair(DataPointType.PRESSURE, { dataPoint -> dataPoint.mean >= 650.0 && dataPoint.mean < 800.0 }),
                Pair(DataPointType.CLOUDCOVER, { dataPoint -> dataPoint.mean >= 0 && dataPoint.mean < 100.0 }),
                Pair(DataPointType.PRECIPITATION, { dataPoint -> dataPoint.mean >= 0 && dataPoint.mean < 100.0 })
        )

        private val typesWithBuilderMethods = listOf(
                Pair(DataPointType.WIND, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) }),
                Pair(DataPointType.TEMPERATURE, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) }),
                Pair(DataPointType.HUMIDITY, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) }),
                Pair(DataPointType.PRESSURE, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) }),
                Pair(DataPointType.CLOUDCOVER, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) }),
                Pair(DataPointType.PRECIPITATION, { data: AtmosphericData, point: DataPoint -> data.copy(wind = point) })
        )

    }
}
