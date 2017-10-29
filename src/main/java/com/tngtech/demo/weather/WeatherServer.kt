package com.tngtech.demo.weather

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer

@SpringBootApplication
@EnableAutoConfiguration
open class WeatherServer : SpringBootServletInitializer() {
    companion object {

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(WeatherServer::class.java, *args)
        }
    }
}
