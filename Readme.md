[![Build Status](https://travis-ci.org/wuan/rest-demo-jersey-kotlin.svg?branch=master)](https://travis-ci.org/wuan/rest-demo-jersey-kotlin)
[![Coverage Status](https://coveralls.io/repos/github/wuan/rest-demo-jersey-kotlin/badge.svg?branch=master)](https://coveralls.io/github/wuan/rest-demo-jersey-kotlin?branch=master)

# rest-demo-jersey-kotlin
Spring-boot and Jersey based REST service written in Kotlin showing the integration of [rest-schemagen](http://github.com/Mercateo/rest-schemagen).

The Java variant can be found here: [https://github.com/TNG/rest-demo-jersey](https://github.com/TNG/rest-demo-jersey)

<img src="https://rawgit.com/TNG/rest-demo-jersey/master/doc/service.svg" alt="service schematics">

## Start instructions

 * run service

   ``` mvn spring-boot:run ```
   
 * the Swagger UI can be found on http://localhost:9090/
 * REST API is available at http://localhost:9090/api/
    
## Example usage

### Get base info
GET: http://localhost:9090/api

* stations: GET /api/stations 
* statistics: GET /api/weather/statistics 
* query: GET /api/weather/49.0/11.0 

### Create station
POST: http://localhost:9090/api/stations

* self: GET /api/stations/d6b4cf17-144c-443d-8ca3-2ed2bc371ef3 
* delete: DELETE /api/stations/d6b4cf17-144c-443d-8ca3-2ed2bc371ef3

### Get stations
GET: http://localhost:9090/api/stations

* create: POST /api/stations {u'type': u'object', u'properties': {u'latitude': {u'type': u'number'}, u'name': {u'type': u'string'}, u'longitude': {u'type': u'number'}}}
* self: GET /api/stations?offset=0&limit=100 
