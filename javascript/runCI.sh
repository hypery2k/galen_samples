#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

cd shoppingcart/galen-tests
galen test . -DwebsiteUrl=${websiteUrl} --htmlreport ../../../reports/shopping-cart
cd ../..

cd bootstrap
galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/local -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver
galen test saucelabs.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/saucelabs