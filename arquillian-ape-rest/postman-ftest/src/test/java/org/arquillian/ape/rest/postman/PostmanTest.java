package org.arquillian.ape.rest.postman;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.arquillian.ape.rest.RestPopulator;
import org.arquillian.cube.DockerUrl;
import org.arquillian.cube.HealthCheck;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.HostPort;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.restassured.builder.RequestSpecBuilder;

@ExtendWith(ArquillianExtension.class)
@HealthCheck("/message")
@Disabled("FIXME this fails on travis-ci due to docker/cube not able to start/connect to the container: http://bit.ly/2r82st1")
class PostmanTest {

    @ArquillianResource
    @DockerUrl(containerName = "messenger", exposedPort = 8080)
    RequestSpecBuilder requestSpecBuilder;

    @HostIp
    String hostIp;

    @HostPort(containerName = "messenger", value = 8080)
    int port;

    @ArquillianResource
    @Postman
    RestPopulator populator;

    @Test
    void should_get_messages() {

        populator.forServer(hostIp, port)
                .usingDataSets("/message.json")
                .execute();

        given()
                .spec(requestSpecBuilder.build())
                .when()
                .get("/message")
                .then()
                .assertThat().body(is("Hello From Populator Test"));
    }
}
