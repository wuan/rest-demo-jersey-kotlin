package com.tngtech.demo.weather.lib

import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class CsvSplitterTest {

    private val splitter = CsvSplitter()

    @Test
    fun splitterShouldSplitAtComma() {
        assertThat(splitter.split("foo,bar,baz")).containsExactly("foo", "bar", "baz")
    }

    @Test
    fun splitterShouldIgnoreCommaInsideQuote() {
        assertThat(splitter.split("\"foo,bar\",baz")).containsExactly("\"foo,bar\"", "baz")
    }
}