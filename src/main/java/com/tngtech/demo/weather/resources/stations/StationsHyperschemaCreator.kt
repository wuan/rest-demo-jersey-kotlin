package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.types.*
import com.tngtech.demo.weather.domain.Station
import org.springframework.stereotype.Component

@Component
class StationsHyperschemaCreator(
        private val stationLinkCreator: StationLinkCreator,
        private val stationsLinkCreator: StationsLinkCreator,
        private val responseBuilderCreator: PaginatedResponseBuilderCreator,
        private val hyperSchemaCreator: HyperSchemaCreator
) {

    fun createPaginatedResponse(paginatedStations: PaginatedList<WithId<Station>>): PaginatedResponse<WithId<Station>> {
        return responseBuilderCreator.builder<WithId<Station>, WithId<Station>>()
                .withList(paginatedStations)
                .withPaginationLinkCreator { rel, offset, limit -> stationsLinkCreator.create(rel, offset, limit) }
                .withContainerLinks(
                        stationsLinkCreator.forCreate()
                )
                .withElementMapper { station -> hyperSchemaCreator.create(station, stationLinkCreator.createFor(station.id)) }
                .build()
    }

    fun create(stationWithId: WithId<Station>): ObjectWithSchema<WithId<Station>> {
        return hyperSchemaCreator.create(stationWithId, stationLinkCreator.createFor(stationWithId.id))
    }
}
