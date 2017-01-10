# Beacon Implementation [![Build Status](https://travis-ci.org/mcupak/beacon-java.svg?branch=develop)](https://travis-ci.org/mcupak/beacon-java) [![GitHub license](https://img.shields.io/badge/license-mit-blue.svg)](https://raw.githubusercontent.com/mcupak/beacon-java/develop/LICENSE)

JBDK (Java Beacon Development Kit) provides a Java EE implementation of the [Beacon API](https://github.com/ga4gh/beacon-team/), which allows you to light a Beacon with minimal effort. The BDK provides common functionality required by beacons, such as a REST API implementation compatible with the Beacon API, a sample beacon implementation, parameter conversion, data serialization, exception handling, test suite and more.

## Structure
The setup is split into the following modules:

- [beacon-java-rest](/beacon-java-rest) - REST API.
- [beacon-java-service](/beacon-java-service) - business logic.
- [sample-beacon-adapter](/sample-beacon-adapter) - sample adapter implementation.

## Building
Prerequisites: Java 8+, Maven 3+, an application server with Java EE 7 support (e.g. WildFly 8+), [Beacon data model](https://github.com/ga4gh/beacon-team).

Build the project:

    mvn install

In order to run the tests from beacon-java-rest in a managed (remote) container, use the test-managed (test-remote) Maven profile. Example:

    mvn test -Ptest-managed

## Adding data
The BDK supports any implementation of the [Beacon Adapter API](https://github.com/mcupak/beacon-adapter-api). You can provide your own custom adapter by extending `BeaconAdapter`, or use one of the available implementations for common data sources, such as [GA4GH Variants API](https://github.com/mcupak/beacon-adapter-variants), [GA4GH Annotations API](https://github.com/mcupak/beacon-adapter-annotations), or [VCF](https://github.com/mcupak/beacon-adapter-vcf). [Sample adapter](/sample-beacon-adapter) is provided as an example and a starting point for a custom implementation.

## Running
For WildFly, start the server:

    $JBOSS_HOME/bin/standalone.sh

And deploy the WAR file:

    cp beacon-java-rest/target/beacon-java-rest.war $JBOSS_HOME/standalone/deployments/

By default, the application will be running on <http://localhost:8080/beacon-java>. Endpoints provided:

    http://localhost:8080/beacon-java - information about your beacon
    http://localhost:8080/beacon-java/query - access to query service
