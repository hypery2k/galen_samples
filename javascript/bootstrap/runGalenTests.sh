#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap -Dwebdriver.chrome.driver=/opt/dev/chromedriver
open  ../../reports/bootstrap/report.html