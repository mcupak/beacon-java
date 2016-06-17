package com.dnastack.beacon.rest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import org.ga4gh.beacon.Beacon;
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
import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by patrickmagee on 2016-06-16.
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
        WebArchive war = ShrinkWrap
                .create(MavenImporter.class)
                .loadPomFromFile("pom.xml")
                .importBuildOutput()
                .as(WebArchive.class);
        System.out.println("WAR name: " + war.getName());
        return war;
    }

    @Test
    public void testGetBeacon() {

        System.out.println(given().accept(ContentType.JSON).get(baseUrl).then().extract().asString());
    }

    @Test
    public void testGetAllele() throws InterruptedException {
        Thread.sleep(123123112);
        String out = given()
                .accept(ContentType.JSON)
                .queryParam("referenceName", "test")
                .queryParam("start", 1231)
                .queryParam("referenceBases", "ACA")
                .queryParam("alternateBases", "SCSC")
                .queryParam("assemblyId", "GRC123")
                .queryParam("datasetIds", Arrays.asList("asd"))
                .queryParam("includeDatasetResponses", true)
                .get(baseUrl + "query")
                .then()
                .extract()
                .asString();

        System.out.println(out);
    }

}
