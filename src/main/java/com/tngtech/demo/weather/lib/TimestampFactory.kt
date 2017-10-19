package com.tngtech.demo.weather.lib

import org.springframework.stereotype.Component

@Component
class TimestampFactory {
    val currentTimestamp: Long
        get() = System.currentTimeMillis()
}
