#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

cd testSuite

cd shoppingcart/galen-tests
galen test . -DwebsiteUrl=${websiteUrl} --htmlreport ../../../reports/shopping-cart --testngreport ../../../reports/shopping-cart/testng.xml
cd ../..

cd bootstrap
galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/testsuite --testngreport ../../reports/bootstrap/testsuite/testng.xml -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver
galen test saucelabs.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/saucelabsTestsuite --testngreport ../../reports/bootstrap/saucelabsTestsuite/testng.xml

cd ../..

cd testRunner

cd bootstrap
galen test . --htmlreport ../../reports/bootstrap/testrunner/ --testngreport ../../reports/bootstrap/testrunner/testng.xml -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver
