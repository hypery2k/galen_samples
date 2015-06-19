#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

cd shoppingcart/galen-tests
galen test . -DwebsiteUrl=${websiteUrl} --htmlreport ../../../reports/shopping-cart --testngreport ../../../reports/shopping-cart/testng.xml
cd ../..

cd bootstrap
galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/local --testngreport ../../reports/bootstrap/local/testng.xml -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver
galen test saucelabs.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/saucelabs --testngreport ../../reports/bootstrap/saucelabs/testng.xml