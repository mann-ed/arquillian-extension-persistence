package org.arquillian.ape.nosql.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.HostPort;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import redis.clients.jedis.Jedis;

@ExtendWith(ArquillianExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
class RedisTest {

    @ArquillianResource
    @Redis
    NoSqlPopulator populator;

    @HostPort(containerName = "redis", value = 6379)
    int port;

    @HostIp
    private String hostIp;

    @Test
    void should_populate_redis() {
        populator.forServer(hostIp, port)
                .usingDataSet("books.json")
                .execute();

        try (final Jedis jedis = new Jedis(hostIp, port)) {
            final Map<String, String> fieldsOfTheHobbitBook = jedis.hgetAll("The Hobbit");

            assertThat(fieldsOfTheHobbitBook)
                    .containsEntry("title", "The Hobbit")
                    .containsEntry("numberOfPages", "293");
        }
    }

    @AfterEach
    void cleanDatabase() {
        populator.forServer(hostIp, port)
                .clean();
    }
}
