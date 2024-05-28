package org.arquillian.ape.nosql.couchdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.util.Map;

import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.Container;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.DockerContainer;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
class CouchDbTest {

    @CouchDb
    @ArquillianResource
    NoSqlPopulator populator;

    @DockerContainer
    Container couchdb = Container.withContainerName("couchdb")
            .fromImage("fedora/couchdb")
            .withPortBinding(5984)
            .build();

    @Test
    void should_populate_couchdb() throws MalformedURLException {
        populator.forServer(couchdb.getIpAddress(), couchdb.getBindPort(5984))
                .withStorage("test")
                .usingDataSet("books.json")
                .execute();

        final StdHttpClient.Builder httpBuilder = new StdHttpClient.Builder();
        httpBuilder.url("http://" + couchdb.getIpAddress() + ":" + couchdb.getBindPort(5984));
        httpBuilder.caching(true);
        final CouchDbInstance  dbInstance       = new StdCouchDbInstance(httpBuilder.build());
        final CouchDbConnector couchDbConnector = dbInstance.createConnector("test", true);

        final Map<String, Object> fieldsOfTheHobbitBook = couchDbConnector.get(Map.class, "1");

        assertThat(fieldsOfTheHobbitBook)
                .containsEntry("title", "The Hobbit")
                .containsEntry("numberOfPages", 293);
    }

}
