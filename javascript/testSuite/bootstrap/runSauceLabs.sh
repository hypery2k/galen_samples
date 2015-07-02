#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

galen test saucelabs.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/saucelabs
open  ../../reports/bootstrap/saucelabs/report.html