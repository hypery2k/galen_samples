# Galen samples for Maven and JUnit

A showcase of Maven + JUnit + Galen usage

Jenkins Sample Build: [![Build Status](https://martinreinhardt-online.de/jenkins/buildStatus/icon?job=Galen/Galen_sample_JUnit)](https://martinreinhardt-online.de/jenkins/job/Galen/job/Galen_sample_JUnit/) 

## Steps touse this sample for your own

You need Maven 3+ installed.

* Download the [repo](https://github.com/hypery2k/galen_samples/archive/master.zip)
* Unzip the folder
* Open command prompt in extracted folder and go to the junit folder and run the tests:
```
  cd junit
  mvn verify
```

## Extend the sample

* To extend the project, run the following command:
```
  cd junit
  mvn eclipse:eclipse idea:idea
```
* Import the Junit project (junit-folder) in IDEA and Eclipse
