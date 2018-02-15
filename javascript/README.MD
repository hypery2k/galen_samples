# Galen samples for JavaScript


## Setup

```bash
(sudo) npm install -g galenframework-cli

```

## Execution

A showcase of JavaScript + Galen usage

Jenkins Sample Build: [![Build Status](https://martinreinhardt-online.de/jenkins/buildStatus/icon?job=Galen/Galen_sample_JavaScript)](https://martinreinhardt-online.de/jenkins/job/Galen/job/Galen_sample_JavaScript/)

The sample script [runCI.sh](runCI.sh) just wrap up some calls for CI build within Jenkins:

```bash
galen test . -DwebsiteUrl=${websiteUrl} --htmlreport ../../../reports/shopping-cart --testngreport ../../../reports/shopping-cart/testng.xml  -Dwebdriver.chrome.driver=/opt/dev/chromedriver

```
The following arguments are used:

* -DwebsiteUrl - a url of page for Galen to test on (passed to test file)
* -Dwebdriver.chrome.driver - path the ChromeDriver
* --htmlreport - path to folder in which Galen should generate html reports
* --testngreport - path to xml file in which Galen should write testng report
