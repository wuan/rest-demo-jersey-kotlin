package com.tngtech.demo.weather.lib

class CsvSplitter {
    fun split(line: String): Array<String> {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()).toTypedArray()
    }
}
