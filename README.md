# Java Beacon Implementation (JBDK) [![Build Status](https://travis-ci.org/mcupak/beacon-java.svg?branch=develop)](https://travis-ci.org/mcupak/beacon-java) [![GitHub license](https://img.shields.io/badge/license-mit-blue.svg)](https://raw.githubusercontent.com/mcupak/beacon-java/develop/LICENSE)

## Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [Modules](#Modules)
* [Beacon Specification](#beacon-specification)
* [How it works](#how-it-works)
* [Technologies](#technologies)

## What it is
This project contains the BDK (beacon development kit) for Java EE developers. The BDK provides all of the tools required
to create a basic rest implementation for a beacon almost straight out of the box with only minimal set up required.
Extend the BDK to make it fit within their own project, or use it as is defining your own adapter to retrieve data from
your preferred data source.

## System Requirements
- Java 8 or later;
- Maven 3.0 or later;
- [Beacon Schemas (v.0.3.0)](https://github.com/ga4gh/beacon-team): BDK relies on the generated Java classes;
- an application server with support for Java EE 7 (e.g. JBoss EAP or WildFly).

## Modules
- [beacon-java-rest](/beacon-java-rest): rest implementation of a beacon, configured to run on a JBoss server;
- [sample-beacon-adapter](/sample-beacon-adapter): sample beacon adapter implementation.

## How to run it
Build the project:

    mvn clean install

Start the JBoss server:

    $JBOSS_HOME/bin/standalone.sh

Deploy beacon-java-rest:

    cp beacon-java-rest/target/beacon-java-rest.war $JBOSS_HOME/standalone/deployments/

The application should now be running on <http://localhost:8080/beacon-java>.

In order to run the tests from beacon-java-rest in a managed (remote) container, use the test-managed (test-remote) Maven profile. Example:

    mvn test -Ptest-managed

## Creating a Beacon
Beacon REST API uses Beacon Adapter to retrieve the beacon data. The BDK ships with a sample Beacon Adapter that uses
sample data. The sample Beacon Adapter is intended to be a placeholder to allow the tests to be run, and to give the user
an idea of how to implement a beacon.

Once you are ready, implement your own [beacon-adapter](https://github.com/mcupak/beacon-service) project to query your
own data. To switch to your own Beacon Adapter, simply replace the adapter dependency in the beacon-java-rest's pom with
your own.

The Rest API provides the following endpoints upon deployment of your beacon:

    http://localhost:8080/beacon-java - information about your beacon
    http://localhost:8080/beacon-java/query - access to query service


## Technologies
Java EE. CDI, JAX-RS, JAXB. Tested with Arquillian/ShrinkWrap.