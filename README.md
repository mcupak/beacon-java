# Java Beacon Rest

## Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [Modules](#Modules)
* [How to run it](#how-to-run-it)
* [How it works](#how-it-works)
* [Technologies](#technologies)

##What it is
This project contains BDK (beacon development kit) for Java (EE) developers. It provides a rest implementation of a simple beacon allowing the developers to plug in their own data/functionality throught the use of the BeaconAdapter interface. The API ensure the respoinse is compliant with the Beacon Spec.

##System requirements
All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later. Since the project is Java EE based, an application server with support for Java EE 7 is needed to deploy the application (e.g. JBoss EAP or WildFly).

##Modules
1. [beacon-java-core](/beacon-java-core)
    Contains core classes and functions shared across modules in the BJDK.
2. [beacon-java-rest](/beacon-java-rest)
    Rest implementation of a beacon, configured to run on a JBOSS server.


##How to run it
Start the JBoss server:

    For Linux/Unix: JBOSS_HOME/bin/standalone.sh
    For Windows: 	JBOSS_HOME\bin\standalone.bat

Build and deploy the archive:

    mvn clean install jboss-as:deploy

The application should now be running on <http://localhost:8080/beacon-java>.

In order to run the tests in a managed (remote) container, use the test-managed (test-remote) Maven profile. Example:

    mvn clean test -Ptest-managed

##How it works
The project provides the following:
- API for beacons
- sample beacon implementation
- conversion of parameters to a normalized form (the same way Beacon of Beacons does)
- sample navigation webpage
- sample testsuite


##Technologies
Java EE. CDI, JAX-RS, JAXB. Tested with Arquillian/ShrinkWrap.