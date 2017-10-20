package com.tngtech.demo.weather.resources

import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.tngtech.demo.weather.resources.stations.StationsLinkCreator
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator
import org.springframework.stereotype.Component

@Component
class RootHyperSchemaCreator(
        private val stationsLinkCreator: StationsLinkCreator,
        private val weatherLinkCreator: WeatherLinkCreator,
        private val hyperSchemaCreator: HyperSchemaCreator

) {

    fun create(): ObjectWithSchema<Void> {
        return hyperSchemaCreator.create(null,
                stationsLinkCreator.create(null, null),
                weatherLinkCreator.create())
    }
}
