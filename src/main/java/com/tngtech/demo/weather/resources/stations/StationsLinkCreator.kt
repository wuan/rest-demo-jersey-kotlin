package com.tngtech.demo.weather.resources.stations

import com.mercateo.common.rest.schemagen.link.LinkFactory
import com.mercateo.common.rest.schemagen.link.relation.Relation
import com.mercateo.common.rest.schemagen.link.relation.RelationContainer
import com.mercateo.common.rest.schemagen.util.OptionalUtil.collect
import com.tngtech.demo.weather.Rel
import com.tngtech.demo.weather.resources.WeatherRel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.inject.Named
import javax.ws.rs.core.Link

@Component
class StationsLinkCreator @Autowired
constructor(@param:Named("stationsLinkFactory") private val stationsLinkFactory: LinkFactory<StationsResource>) {

    fun create(offset: Int?, limit: Int?): List<Link> {
        return collect(
                create(WeatherRel.STATIONS, offset, limit)
        )
    }

    fun create(rel: RelationContainer, offset: Int?, limit: Int?): Optional<Link> {
        return stationsLinkFactory.forCall(rel.relation) { r -> r.getStations(offset, limit) }
    }

    fun forCreate(): Optional<Link> {
        return stationsLinkFactory.forCall(Rel.CREATE.relation) { r -> r.addStation(null) }
    }
}
