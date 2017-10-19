package com.tngtech.demo.weather.lib

import io.vavr.Tuple2
import io.vavr.kotlin.tuple
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class EventCounter<T> {
    private val countByEvent = ConcurrentHashMap<T, AtomicInteger>()

    fun increment(event: T): Int {
        return countByEvent
                .computeIfAbsent(event) { k -> AtomicInteger() }
                .incrementAndGet()
    }

    fun countOf(event: T): Int {
        return countByEvent[event]?.get() ?: 0
    }

    fun fractionOf(event: T): Double {
        val totalCount = totalCount()
        return if (totalCount != 0)
            countOf(event).toDouble() / totalCount
        else
            0.0
    }

    fun totalCount(): Int {
        return countByEvent.values
                .map { it.get() }
                .fold(0, { a, b -> a + b })
    }

    val events: Set<T>
        get() {
            return countByEvent.keys.toSet()
        }

    fun stream(): Sequence<Tuple2<T, AtomicInteger>> {
        return countByEvent.entries
                .map { entry -> tuple(entry.key, entry.value) }
                .asSequence()
    }
}
