package com.tngtech.demo.weather.domain.measurement

/**
 * A collected point, including some information about the range of collected values
 */

data class DataPoint(
        val type: DataPointType,
        /**
         * the mean of the observations
         */
        val mean: Double,
        /**
         * 1st quartile -- useful as a lower bound
         */
        val first: Int = 0,
        /**
         * 2nd quartile -- median value
         */
        val median: Int = 0,
        /**
         * 3rd quartile value -- less noisy upper value
         */
        val last: Int = 0,
        /**
         * the total number of measurements
         */
        val count: Int = 0)
