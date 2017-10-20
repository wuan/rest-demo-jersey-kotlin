package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator
import org.springframework.stereotype.Component

@Component
class StationHyperSchemaCreator(
        private val hyperSchemaCreator: HyperSchemaCreator,
        private val stationLinkCreator: StationLinkCreator,
        private val weatherLinkCreator: WeatherLinkCreator
) {

    fun create(station: WithId<Station>): ObjectWithSchema<WithId<Station>> {
        return hyperSchemaCreator.create(
                station,
                stationLinkCreator.createFor(station.id),
                weatherLinkCreator.createForStation(station.id)
        )
    }
}
