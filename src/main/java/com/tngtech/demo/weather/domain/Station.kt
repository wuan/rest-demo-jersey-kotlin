package com.tngtech.demo.weather.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.tngtech.demo.weather.domain.gis.Point
import lombok.Builder
import lombok.EqualsAndHashCode
import lombok.ToString

data class Station @JsonCreator
constructor(
        @param:JsonProperty("name") val name: String,
        @param:JsonProperty("latitude") override val latitude: Double,
        @param:JsonProperty("longitude") override val longitude: Double) : Point
