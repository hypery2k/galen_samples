#!/bin/bash

currentDir=$( pwd )
websiteUrl=file://${currentDir}/website/shopping-cart.html

cd galen-tests

galen test . -DwebsiteUrl=${websiteUrl} --htmlreport ../../../reports/shopping-cart


