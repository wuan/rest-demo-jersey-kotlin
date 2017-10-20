package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.link.LinkFactory
import com.mercateo.common.rest.schemagen.link.relation.Rel
import com.mercateo.common.rest.schemagen.util.OptionalUtil.collect
import com.tngtech.demo.weather.forCall
import org.springframework.stereotype.Component
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.ws.rs.core.Link


@Component
class StationLinkCreator @Inject
constructor(@param:Named("stationLinkFactory") private val stationLinkFactory: LinkFactory<StationResource>) {

    fun createFor(stationId: UUID): List<Link> {
        return collect(
                stationLinkFactory.forCall(Rel.SELF) { r: StationResource -> r.getStation(stationId) },
                stationLinkFactory.forCall(Rel.DELETE) { r: StationResource -> r.deleteStation(stationId) }
        )
    }
}
