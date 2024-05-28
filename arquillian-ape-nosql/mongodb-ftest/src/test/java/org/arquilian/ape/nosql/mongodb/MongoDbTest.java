package org.arquilian.ape.nosql.mongodb;

import static org.assertj.core.api.Assertions.assertThat;

import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.ape.nosql.mongodb.MongoDb;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.HostPort;
import org.bson.Document;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ExtendWith(ArquillianExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
class MongoDbTest {

    @HostPort(containerName = "mongodb", value = 27017)
    int            port;
    @ArquillianResource
    @MongoDb
    NoSqlPopulator populator;
    @HostIp
    private String hostIp;

    @Test
    void should_populate_mongodb() {
        populator.forServer(hostIp, port)
                .withStorage("test")
                .usingDataSet("books.json")
                .execute();

        try (final MongoClient mongoClient = new MongoClient(hostIp, port)) {
            final MongoDatabase             database  = mongoClient.getDatabase("test");
            final MongoCollection<Document> book      = database.getCollection("Book");
            final FindIterable<Document>    documents = book.find();

            assertThat(documents.first())
                    .containsEntry("title", "The Hobbit")
                    .containsEntry("numberOfPages", 293);
        }
    }

    @AfterEach
    public void cleanDatabase() {
        populator.forServer(hostIp, port)
                .withStorage("test")
                .clean();
    }
}
