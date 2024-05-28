package org.arquillian.ape.nosql.polyglot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.arquillian.ape.api.Server;
import org.arquillian.ape.api.UsingDataSet;
import org.arquillian.ape.junit.extension.DeclarativeArquillianPersistenceExtension;
import org.arquillian.ape.nosql.mongodb.MongoDb;
import org.arquillian.ape.nosql.redis.Redis;
import org.arquillian.cube.docker.junit5.ContainerDsl;
import org.arquillian.cube.docker.junit5.ContainerDslResolver;
import org.bson.Document;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

@Server(host = "${arq.cube.docker.redis_3_2_6.ip}", port = "${arq.cube.docker.redis_3_2_6.port.6379:6379}", type = Redis.class)
@Server(host = "${arq.cube.docker.mongo_3_2_12.ip}", port = "${arq.cube.docker.mongo_3_2_12.port.27017:27017}", storage = "test", type = MongoDb.class)
@ExtendWith(ArquillianExtension.class)
@ExtendWith(ContainerDslResolver.class)
@ExtendWith(DeclarativeArquillianPersistenceExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
public class PolyglotPersistenceTest {

    public static ContainerDsl redis = new ContainerDsl("redis:3.2.6")
            .withPortBinding(6379);

    public static ContainerDsl mongo = new ContainerDsl("mongo:3.2.12")
            .withPortBinding(27017);

    @Test
    @UsingDataSet(value = "booksRedis.json", type = Redis.class)
    @UsingDataSet(value = "booksMongo.json", type = MongoDb.class)
    void should_populate_redis() {

        final int    redisPort = Integer.parseInt(System.getProperty("arq.cube.docker.redis_3_2_6.port.6379"));
        final String redisHost = System.getProperty("arq.cube.docker.redis_3_2_6.ip");

        final Jedis               jedis                 = new Jedis(redisHost, redisPort);
        final Map<String, String> fieldsOfTheHobbitBook = jedis.hgetAll("The Hobbit");

        assertThat(fieldsOfTheHobbitBook)
                .containsEntry("title", "The Hobbit")
                .containsEntry("numberOfPages", "293");

        final int    mongoPort = Integer.parseInt(System.getProperty("arq.cube.docker.mongo_3_2_12.port.27017"));
        final String mongoHost = System.getProperty("arq.cube.docker.mongo_3_2_12.ip");

        final MongoClient               mongoClient = new MongoClient(mongoHost, mongoPort);
        final MongoDatabase             database    = mongoClient.getDatabase("test");
        final MongoCollection<Document> book        = database.getCollection("Book");
        final FindIterable<Document>    documents   = book.find();

        assertThat(documents.first())
                .containsEntry("title", "The Lord Of The Rings")
                .containsEntry("numberOfPages", 1184);
    }
}
