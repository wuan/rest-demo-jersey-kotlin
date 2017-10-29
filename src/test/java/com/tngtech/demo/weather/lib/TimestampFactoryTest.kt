package com.tngtech.demo.weather.lib

import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class TimestampFactoryTest {

    @Test
    fun shouldCreateCurrentTimestamp() {
        val currentTimeMillis = System.currentTimeMillis()
        assertThat(TimestampFactory().currentTimestamp).isBetween(currentTimeMillis, currentTimeMillis + 10)
    }

}