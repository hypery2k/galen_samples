#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

galen test . --htmlreport ../../reports/bootstrap/testrunner -Dwebdriver.chrome.driver=/opt/dev/chromedriver
open  ../../reports/bootstrap/testrunner/report.html
