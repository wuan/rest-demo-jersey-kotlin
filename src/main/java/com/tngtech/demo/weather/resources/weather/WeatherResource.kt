package com.tngtech.demo.weather.resources.weather

import com.mercateo.common.rest.schemagen.JerseyResource
import com.tngtech.demo.weather.domain.gis.Location
import com.tngtech.demo.weather.domain.measurement.AtmosphericData
import com.tngtech.demo.weather.domain.measurement.DataPoint
import com.tngtech.demo.weather.repositories.StationRepository
import com.tngtech.demo.weather.repositories.WeatherDataRepository
import com.tngtech.demo.weather.resources.Paths
import com.tngtech.demo.weather.resources.Paths.RADIUS
import com.tngtech.demo.weather.resources.Paths.STATION_ID
import com.tngtech.demo.weather.resources.Paths.STATISTICS
import io.swagger.annotations.Api
import lombok.AllArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * A REST implementation of the WeatherCollector API. Accessible only to station
 * weather collection sites via secure VPN.
 */

@Path(Paths.WEATHER)
@Component
@AllArgsConstructor
@Api(value = Paths.WEATHER, description = "weather resource")
@Slf4j
open class WeatherResource(
        private val stationRepository: StationRepository,
        private val weatherDataRepository: WeatherDataRepository,
        private val handler: WeatherController
) : JerseyResource {

    companion object {
        val log = LoggerFactory.getLogger(WeatherResource::class.java)
    }


    /**
     * Retrieve health and status information for the the query api. Returns information about how the number
     * of datapoints currently held in memory, the frequency of requests for each IATA code and the frequency of
     * requests for each radius.
     *
     * @return a JSON formatted dict with health information.
     */
    open val statistics: Map<String, Any>
        @GET
        @Path(STATISTICS)
        @Produces(MediaType.APPLICATION_JSON)
        get() {
            log.debug("getStatistics()")

            return handler.statistics
        }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    open fun queryWeatherByLocation(@BeanParam queryParam: WeatherQueryParam): List<AtmosphericData> {
        val location = Location(queryParam.latitude!!, queryParam.longitude!!)
        return handler.queryWeather(location, queryParam.radius)
    }

    /**
     * Update the airports atmospheric information for a particular pointType with
     * json formatted data point information.
     *
     * @param stationId the station name
     * @param dataPoint data structure containing point type and mean, first, second, third and count values
     * @return HTTP Response code
     */
    @POST
    @Path("/{" + Paths.STATION_ID + "}")
    open fun updateWeather(@PathParam(Paths.STATION_ID) stationId: UUID,
                      dataPoint: DataPoint?): Response {

        if (stationRepository.getStationById(stationId) == null) {
            log.debug("updateWeather({}, {}, {}) not accepted", stationId, dataPoint)
            return Response.status(Response.Status.NOT_ACCEPTABLE).build()
        }
        log.debug("updateWeather({}, {}, {})", stationId, dataPoint)

        return if (dataPoint != null) {
            weatherDataRepository.update(stationId, dataPoint)

            Response.status(Response.Status.OK).build()
        } else {
            Response.status(Response.Status.BAD_REQUEST).build()
        }
    }


    /**
     * Retrieve the most up to date atmospheric information from the given station and other airports in the given
     * radius.
     *
     * @param stationId the station name
     * @param radius    the radius, in km, from which to collect weather data
     * @return an HTTP Response and a list of [AtmosphericData] from the requested station and
     * stations in the given radius
     */
    @GET
    @Path("/{$STATION_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    open fun queryWeatherByStation(@PathParam(STATION_ID) stationId: UUID,
                              @QueryParam(RADIUS) @DefaultValue("0.0") radius: Double?): List<AtmosphericData> {
        var radius = radius
        log.debug("weather({}, {})", stationId, radius)

        radius = if (radius == null) 0.0 else radius

        return handler.queryWeather(stationId, radius)
    }

}
