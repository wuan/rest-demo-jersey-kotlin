#!/usr/bin/env python

from hyperschema import Link
from requests import Session

base_url = "http://localhost:9090/api"

session = Session()
#  session.verify = False
session.headers['content-type'] = 'application/json'

root = Link(base_url, session=session).follow()

stations = root.follow('stations')

print('*** create station')

station = stations.follow('create', {'name': 'test', 'longitude': 11.0, 'latitude': 49.0})
print("created: {}".format(station))

stations = stations.follow('self')
print(stations)

station.follow('delete')

stations = stations.follow('self')
print(stations)


