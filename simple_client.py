#!/usr/bin/env python

from hyperschema import Link
from requests import Session

base_url = "http://localhost:9090/api"

session = Session()
#  session.verify = False
session.headers['content-type'] = 'application/json'

root = Link(base_url, session=session).follow()

stations = root.follow('stations')

new_station = stations.follow('create', {'name': 'test', 'longitude': 11.0, 'latitude': 49.0})
print("created: {}".format(new_station))

stations = stations.follow('self')
for station in stations:
    print("   ", station.data)

new_station.follow('update', {'type': 'TEMPERATURE', 'mean': 22.0, 'count':15})

query_result = new_station.follow('query')
for result in query_result:
    print("   ", result)

new_station.follow('delete')

stations = stations.follow('self')
print(stations)


