package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.JerseyResource
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.repositories.StationRepository
import com.tngtech.demo.weather.repositories.WeatherDataRepository
import com.tngtech.demo.weather.resources.Paths.STATION_ID
import com.tngtech.demo.weather.resources.Roles
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
open class StationResource(
        private val stationRepository: StationRepository,
        private val weatherDataRepository: WeatherDataRepository,
        private val stationHyperSchemaCreator: StationHyperSchemaCreator
) : JerseyResource {

    companion object {
        val log = LoggerFactory.getLogger(StationResource::class.java)
    }


    /**
     * Retrieve station data, including latitude and longitude for a particular station
     *
     * @param stationId the station name
     * @return an HTTP Response with a json representation of [Station]
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    open fun getStation(@PathParam(STATION_ID) stationId: UUID): ObjectWithSchema<WithId<Station>> {
        log.debug("getStation('{})'", stationId)

        return stationRepository.getStationById(stationId)
                ?.let { stationHyperSchemaCreator.create(it) }
                ?: throw NotFoundException("Station with id $stationId was not found")
    }

    /**
     * Remove an station from the known station list
     *
     * @param stationId the station name
     * @return HTTP Repsonse code for the delete operation
     */
    @DELETE
    @RolesAllowed(Roles.ADMIN)
    open fun deleteStation(@PathParam(STATION_ID) stationId: UUID): Response {
        log.debug("deleteStation({})", stationId)

        stationRepository.removeStation(stationId)
        weatherDataRepository.removeStation(stationId)

        return Response.status(Response.Status.NO_CONTENT).build()
    }
}
