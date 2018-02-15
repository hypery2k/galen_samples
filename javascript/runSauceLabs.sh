#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

cd testSuite

cd bootstrap
galen test saucelabs.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/saucelabsTestsuite --testngreport ../../reports/bootstrap/saucelabsTestsuite/testng.xml
