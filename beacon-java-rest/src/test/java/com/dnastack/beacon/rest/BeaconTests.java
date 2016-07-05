/*
 * The MIT License
 *
 *  Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.dnastack.beacon.rest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Test suite run in an arquillian container against a beacon implementation
 *
 * @author patrickmagee.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconTests {

    private static final String API_VERSION = "0.3.0";

    @ArquillianResource
    public URL baseUrl;

    @Rule
    public TestRule watcher = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getClassName() + " - " + description.getMethodName() + "()");
        }
    };

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(MavenImporter.class)
                                   .loadPomFromFile("pom.xml")
                                   .importBuildOutput()
                                   .as(WebArchive.class);
        System.out.println("WAR name: " + war.getName());
        return war;
    }

    /**
     * Test if we can retrieve a beacon, and whether the beacon response is valid
     */
    @Test
    public void testGetBeacon() {

        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);

        assertThat(beacon.getName()).isNotNull();
        assertThat(beacon.getApiVersion()).isEqualTo(API_VERSION);
        assertThat(beacon.getId()).isNotNull();
        assertThat(beacon.getOrganization()).isNotNull();
        assertThat(beacon.getDatasets().size()).isGreaterThanOrEqualTo(1);
    }

    /**
     * Ensure that posts to the beacon endpoint are not supported
     */
    @Test
    public void testPostBeaconNotSupported() {
        given().accept(ContentType.JSON).post(baseUrl).then().assertThat().statusCode(not(200));

    }

    /**
     * Ensure that deletes to the beacon endpoint are not supported
     */
    @Test
    public void testDeleteBeaconNotSupported() {
        given().accept(ContentType.JSON).delete(baseUrl).then().assertThat().statusCode(not(200));
    }

    /**
     * Ensure that puts to the beacon endpoint are not supported
     */
    @Test
    public void testPutBeaconNotSupported() {
        given().accept(ContentType.JSON).put(baseUrl).then().assertThat().statusCode(not(200));
    }

    /**
     * Test to make sure that you can get a BeaconAlleleResponse from /query enpoint, and that it complies
     * with the current beacon spec. Uses the sampleAlleleRequest provided by the beacon
     */
    @Test
    public void testGetAllele() throws InterruptedException {

        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .queryParam("referenceName", request.getReferenceName())
                                          .queryParam("start", request.getStart())
                                          .queryParam("referenceBases", request.getReferenceBases())
                                          .queryParam("alternateBases", request.getAlternateBases())
                                          .queryParam("assemblyId", request.getAssemblyId())
                                          .queryParam("datasetIds", request.getDatasetIds())
                                          .queryParam("includeDatasetResponses", request.getIncludeDatasetResponses())
                                          .get(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getAlleleRequest()).isNotNull();
        assertThat(out.getExists()).isTrue();
        if (request.getIncludeDatasetResponses()) {
            assertThat(out.getDatasetAlleleResponses()).isNotEmpty();
        }
        assertThat(out.getBeaconId()).isEqualTo(beacon.getId());
        assertThat(out.getError()).isNull();
    }

    /**
     * Test to make sure that you can post a BeaconAlleleResponse from /query enpoint, and that it complies
     * with the current beacon spec. Uses the sampleAlleleRequest provided by the beacon
     */
    @Test
    public void testPostAllele() {

        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);

        BeaconAlleleResponse out = given().contentType(ContentType.JSON)
                                          .accept(ContentType.JSON)
                                          .body(request, ObjectMapperType.GSON)
                                          .post(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getAlleleRequest()).isEqualByComparingTo(request);
        assertThat(out.getExists()).isTrue();
        if (request.getIncludeDatasetResponses()) {
            assertThat(out.getDatasetAlleleResponses()).isNotEmpty();
        }
        assertThat(out.getBeaconId()).isEqualTo(beacon.getId());
        assertThat(out.getError()).isNull();

    }

    /**
     * Test to ensure that Delete is not supported
     */
    @Test
    public void testDeleteAlleleNotSupported() {
        given().delete(baseUrl + "query").then().assertThat().statusCode(not(200));
    }

    /**
     * Test to ensure that put is not supported
     */
    @Test
    public void testPutAlleleNotSupported() {
        given().put(baseUrl + "query").then().assertThat().statusCode(not(200));
    }

    /**
     * Test to insure that a post with an invalid request returns a beacon error.
     */
    @Test
    public void testPostInvalidRequest() {
        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);
        request.setReferenceName(null);
        request.setReferenceBases(null);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .contentType(ContentType.JSON)
                                          .body(request, ObjectMapperType.GSON)
                                          .post(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getExists()).isNull();
        assertThat(out.getError()).isNotNull();
        assertThat(out.getError().getErrorCode()).isEqualTo(400);
    }

    /**
     * Test to ensure that a get with missing required params returns a BeaconError
     */
    @Test
    public void testGetAlleleWithMissingRequiredParams() {
        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .queryParam("referenceBases", request.getReferenceBases())
                                          .queryParam("alternateBases", request.getAlternateBases())
                                          .queryParam("assemblyId", request.getAssemblyId())
                                          .queryParam("datasetIds", request.getDatasetIds())
                                          .queryParam("includeDatasetResponses", request.getIncludeDatasetResponses())
                                          .get(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getExists()).isNull();
        assertThat(out.getError()).isNotNull();
        assertThat(out.getError().getErrorCode()).isEqualTo(400);
    }

    /**
     * Test to makes sure that we can still get an allele response if optional parameters are not defined
     */
    @Test
    public void testGetAlleleWithMissingOptionalParams() {
        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);
        request.setIncludeDatasetResponses(false);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .queryParam("referenceName", request.getReferenceName())
                                          .queryParam("start", request.getStart())
                                          .queryParam("referenceBases", request.getReferenceBases())
                                          .queryParam("alternateBases", request.getAlternateBases())
                                          .queryParam("assemblyId", request.getAssemblyId())
                                          .queryParam("datasetIds", request.getDatasetIds())
                                          .get(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getAlleleRequest()).isEqualByComparingTo(request);
        assertThat(out.getExists()).isTrue();
        assertThat(out.getDatasetAlleleResponses()).isNullOrEmpty();

    }

    /**
     * Test to ensure that we can retrieve an allele with the datasets listed
     */
    @Test
    public void testGetAlleleWithDataSets() {
        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .queryParam("referenceName", request.getReferenceName())
                                          .queryParam("start", request.getStart())
                                          .queryParam("referenceBases", request.getReferenceBases())
                                          .queryParam("alternateBases", request.getAlternateBases())
                                          .queryParam("assemblyId", request.getAssemblyId())
                                          .queryParam("datasetIds", request.getDatasetIds())
                                          .queryParam("includeDatasetResponses", request.getIncludeDatasetResponses())
                                          .get(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);
        assertThat(out.getAlleleRequest()).isEqualByComparingTo(request);
        assertThat(out.getExists()).isTrue();
        assertThat(out.getDatasetAlleleResponses()).isNotNull();
        assertThat(out.getDatasetAlleleResponses()).isNotEmpty();

    }

    /**
     * Test to ensure that we can retrieve an allele without the datasets listed
     */
    @Test
    public void testGetAlleleWithoutDatasets() {
        Beacon beacon = given().accept(ContentType.JSON)
                               .get(baseUrl)
                               .then()
                               .extract()
                               .as(Beacon.class, ObjectMapperType.GSON);
        BeaconAlleleRequest request = beacon.getSampleAlleleRequests().get(0);
        request.setIncludeDatasetResponses(false);

        BeaconAlleleResponse out = given().accept(ContentType.JSON)
                                          .queryParam("referenceName", request.getReferenceName())
                                          .queryParam("start", request.getStart())
                                          .queryParam("referenceBases", request.getReferenceBases())
                                          .queryParam("alternateBases", request.getAlternateBases())
                                          .queryParam("assemblyId", request.getAssemblyId())
                                          .queryParam("datasetIds", request.getDatasetIds())
                                          .queryParam("includeDatasetResponses", false)
                                          .get(baseUrl + "query")
                                          .then()
                                          .extract()
                                          .as(BeaconAlleleResponse.class, ObjectMapperType.GSON);

        assertThat(out.getAlleleRequest()).isEqualByComparingTo(request);
        assertThat(out.getExists()).isTrue();
        assertThat(out.getDatasetAlleleResponses()).isNullOrEmpty();
        assertThat(out.getDatasetAlleleResponses()).isNullOrEmpty();
    }

}
