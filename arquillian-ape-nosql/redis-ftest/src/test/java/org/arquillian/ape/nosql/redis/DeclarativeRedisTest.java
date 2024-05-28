package org.arquillian.ape.nosql.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.arquillian.ape.api.Server;
import org.arquillian.ape.api.UsingDataSet;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import redis.clients.jedis.Jedis;

@ExtendWith(ArquillianExtension.class)
@Server(host = "${arq.cube.docker.redis.ip}", port = "${arq.cube.docker.redis.6379:6379}")
@Disabled("arquillian.cube needs to update to jakarta")
class DeclarativeRedisTest {

    @Test
    @UsingDataSet("books.json")
    void should_populate_redis() {
        final Jedis               jedis                 = new Jedis(System.getProperty("arq.cube.docker.redis.ip"),
                6379);
        final Map<String, String> fieldsOfTheHobbitBook = jedis.hgetAll("The Hobbit");

        assertThat(fieldsOfTheHobbitBook)
                .containsEntry("title", "The Hobbit")
                .containsEntry("numberOfPages", "293");
    }

}
