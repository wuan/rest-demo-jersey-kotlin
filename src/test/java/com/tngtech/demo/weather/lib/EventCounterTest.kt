package com.tngtech.demo.weather.lib

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks

import org.assertj.core.api.Assertions.assertThat
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class EventCounterTest {

    @InjectMocks
    private lateinit var eventCounter: EventCounter<Any>

    @Test
    fun emptyEventCounterShouldHaveTotalCountOfZero() {
        assertThat(eventCounter.totalCount()).isEqualTo(0)
    }

    @Test
    fun nonExistentEventShouldHaveACountOfZero() {
        assertThat(eventCounter.countOf("foo")).isEqualTo(0)
    }

    @Test
    fun addingEventsShouldIncreaseTotalCount() {
        eventCounter.increment("foo")
        assertThat(eventCounter.totalCount()).isEqualTo(1)

        eventCounter.increment("bar")
        assertThat(eventCounter.totalCount()).isEqualTo(2)

        eventCounter.increment("foo")
        assertThat(eventCounter.totalCount()).isEqualTo(3)
    }

    @Test
    fun addingEventsShouldIncreaseIndividualCount() {
        eventCounter.increment("foo")
        assertThat(eventCounter.countOf("foo")).isEqualTo(1)

        eventCounter.increment("bar")
        assertThat(eventCounter.countOf("foo")).isEqualTo(1)

        eventCounter.increment("foo")
        assertThat(eventCounter.countOf("foo")).isEqualTo(2)
    }

    @Test
    fun fractionOfEmptyEventCounterShouldReturnZero() {
        assertThat(eventCounter.fractionOf("foo")).isEqualTo(0.0)
    }

    @Test
    fun addingEventsShouldUpdateIndividualFraction() {
        eventCounter.increment("foo")
        assertThat(eventCounter.fractionOf("foo")).isEqualTo(1.0)

        eventCounter.increment("bar")
        assertThat(eventCounter.countOf("foo")).isEqualTo(1)

        eventCounter.increment("foo")
        assertThat(eventCounter.countOf("foo")).isEqualTo(2)
    }
}