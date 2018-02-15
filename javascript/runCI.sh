#!/bin/bash

currentDir=$( pwd )
websiteUrl='http://getbootstrap.com'

cd testSuite

cd bootstrap
galen test basic.test -DwebsiteUrl=${websiteUrl} --htmlreport ../../reports/bootstrap/testsuite --testngreport ../../reports/bootstrap/testsuite/testng.xml -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver

cd ../../testRunner

cd bootstrap
galen test . --htmlreport ../../reports/bootstrap/testrunner/ --testngreport ../../reports/bootstrap/testrunner/testng.xml -Dwebdriver.chrome.driver=/opt/dev/tools/chromedriver

cd ../../gulp
npm test

cd ../../grunt
npm test
