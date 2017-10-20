package com.tngtech.demo.weather.repositories

import com.mercateo.common.rest.schemagen.types.WithId
import com.tngtech.demo.weather.domain.Station
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class StationRepository {
    private val stationsById = ConcurrentHashMap<UUID, Station>()

    val stations: List<WithId<Station>>
        get() = getStations(0, Integer.MAX_VALUE)

    val totalCount: Long
        get() = stationsById.mappingCount()

    fun addStation(newStation: WithId<Station>) {
        stationsById.put(newStation.id, newStation.`object`)
    }

    fun getStationById(stationId: UUID): WithId<Station>? {
        return stationsById[stationId]
                .let { station -> WithId.create(stationId, station) }
    }

    fun getStations(offset: Int?, limit: Int?): List<WithId<Station>> {
        return stationsById.entries
                .drop(offset ?: 0)
                .take(limit ?: 50)
                .map { WithId.create(it) }
    }

    fun removeStation(stationId: UUID) {
        stationsById.remove(stationId)
    }
}
