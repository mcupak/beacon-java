# Java Beacon Rest

## Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to run it](#how-to-run-it)
* [How it works](#how-it-works)
* [Technologies](#technologies)

##What it is
This project contains BDK (beacon development kit) for Java (EE) developers. It provides a rest implementation of a simple beacon allowing the developers to plug in their own data/functionality throught the use of the BeaconAdapter interface. The API ensure the respoinse is compliant with the Beacon Spec.

##System requirements
All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later. Since the project is Java EE based, an application server with support for Java EE 7 is needed to deploy the application (e.g. JBoss EAP or WildFly).

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

##Creating a beacon
The beacon implementation is designed to use a beacon-adapter provided through the [beacon-java-adpaters-core](https://github.com/mcupak/beacon-adapters) project. The user can either extend the abstract BeaconAdapter class to create a custom implementation, or they can use one of the beacon-adapter implementations that are provided
- Add your desired beacon adapter to the pom.xml in the commented out section
- modify index.jsp to provide a landing page for your beacon (optional)
- Deploy The beacon
- Run the test-managed profile to ensure the beacon works and complies with the beacon spec

The API takes care of the rest and provides the following endpoints upon deployment of your beacon:

    http://localhost:8080/beacon-java/info - information about your beacon
    http://localhost:8080/beacon-java/query - access to query service

##Technologies
Java EE. CDI, JAX-RS, JAXB. Tested with Arquillian/ShrinkWrap.
