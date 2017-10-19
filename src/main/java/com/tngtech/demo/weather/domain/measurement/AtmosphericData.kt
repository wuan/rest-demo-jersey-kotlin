package com.tngtech.demo.weather.domain.measurement

data class AtmosphericData(
        /**
         * temperature in degrees celsius
         */
        val temperature: DataPoint? = null,
        /**
         * wind speed in km/h
         */
        val wind: DataPoint? = null,
        /**
         * humidity in percent
         */
        val humidity: DataPoint? = null,
        /**
         * precipitation in cm
         */
        val precipitation: DataPoint? = null,
        /**
         * pressure in mmHg
         */
        val pressure: DataPoint? = null,
        /**
         * cloud cover percent from 0 - 100 (integer)
         */
        val cloudCover: DataPoint? = null,
        /**
         * the last time this data was updated, in milliseconds since UTC epoch
         */
        val lastUpdateTime: Long = 0)
