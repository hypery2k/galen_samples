#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/local -Dwebdriver.chrome.driver=/opt/dev/chromedriver
open  ../../reports/bootstrap/local/report.html