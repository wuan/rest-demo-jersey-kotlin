package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.JerseyResource
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema
import com.mercateo.common.rest.schemagen.types.PaginatedList
import com.mercateo.common.rest.schemagen.types.PaginatedResponse
import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import com.tngtech.demo.weather.repositories.StationRepository
import com.tngtech.demo.weather.resources.Paths
import com.tngtech.demo.weather.resources.Roles
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path(Paths.STATIONS)
@Component
@Api(value = Paths.STATIONS, description = "stations resource")
open class StationsResource(
        private val stationRepository: StationRepository,
        private val stationsHyperschemaCreator: StationsHyperschemaCreator
) : JerseyResource {

    companion object {
        val log = LoggerFactory.getLogger(StationsResource::class.java)
    }


    /**
     * Return a list of known airports as a json formatted list
     *
     * @return HTTP Response code and a json formatted list of IATA codes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    open fun getStations(@QueryParam(Paths.OFFSET) @DefaultValue("0") offset: Int?,
                         @QueryParam(Paths.LIMIT) @DefaultValue("100") limit: Int?): PaginatedResponse<WithId<Station>> {
        var offset = offset
        var limit = limit
        log.debug("getStations({}, {})", offset, limit)
        offset = if (offset == null) 0 else offset
        limit = if (limit == null) 2000 else limit

        val paginatedStations = PaginatedList<WithId<Station>>(
                stationRepository.totalCount.toInt(),
                offset,
                limit,
                stationRepository.getStations(offset, limit))

        return stationsHyperschemaCreator.createPaginatedResponse(paginatedStations)
    }

    /**
     * Add a new station to the known stations list.
     *
     * @param station new station parameters
     * @return HTTP Response code for the add operation
     */
    @POST
    @RolesAllowed(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    open fun addStation(station: Station?): ObjectWithSchema<WithId<Station>> {
        if (station == null) {
            throw NullPointerException()
        }

        log.debug("addStation({}, {}, {})", station.name, station.latitude, station.longitude)

        val stationWithId = WithId.create(station)
        stationRepository.addStation(stationWithId)

        return stationsHyperschemaCreator.create(stationWithId)
    }

    @Path("/{" + Paths.STATION_ID + "}")
    open fun stationSubResource(): Class<StationResource> {
        return StationResource::class.java
    }
}
