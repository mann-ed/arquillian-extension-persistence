package org.arquillian.ape.nosql.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.arquillian.ape.api.Server;
import org.arquillian.ape.api.UsingDataSet;
import org.arquillian.ape.junit.extension.DeclarativeArquillianPersistenceExtension;
import org.arquillian.cube.docker.junit5.ContainerDsl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Server(host = "${arq.cube.docker.redis_3_2_6.ip}", port = "${arq.cube.docker.redis_3_2_6.port.6379:6379}")
@ExtendWith(DeclarativeArquillianPersistenceExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
public class RedisDeclarativeRuleTest {

    public static ContainerDsl redis = new ContainerDsl("redis:3.2.6")
            .withPortBinding(6379);

    @Test
    @UsingDataSet("books.json")
    void should_populate_redis() {

        final BookService         bookService           = new BookService();
        final Map<String, String> fieldsOfTheHobbitBook = bookService.findBookByTitle("The Hobbit");

        assertThat(fieldsOfTheHobbitBook)
                .containsEntry("title", "The Hobbit")
                .containsEntry("numberOfPages", "293");
    }

}
