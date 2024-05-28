package org.arquillian.ape.rdbms.filmlibrary;

import static org.arquillian.ape.rdbms.dbunit.DbUnitOptions.options;
import static org.assertj.db.api.Assertions.assertThat;

import java.net.URI;

import org.arquillian.ape.junit.extension.ArquillianPersistenceExtension;
import org.arquillian.ape.rdbms.core.RdbmsPopulator;
import org.arquillian.ape.rdbms.dbunit.DbUnit;
import org.arquillian.ape.rdbms.flyway.Flyway;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.AwaitBuilder;
import org.arquillian.cube.docker.junit5.ContainerDsl;
import org.arquillian.cube.docker.junit5.ContainerDslResolver;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.Driver;

@ExtendWith(ArquillianPersistenceExtension.class)
@ExtendWith(ContainerDslResolver.class)
@Disabled("arquillian.cube needs to update to jakarta")
public class FilmLibraryTest {

    public static final String DB       = "filmlibrary";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgres";

    private ContainerDsl postgresql = new ContainerDsl("postgres:9.6.2-alpine")
            .withPortBinding("15432->5432")
            .withEnvironment("POSTGRES_PASSWORD", PASSWORD,
                    "POSTGRES_USER", USERNAME,
                    "POSTGRES_DB", DB)
            .withAwaitStrategy(AwaitBuilder.logAwait("LOG:  autovacuum launcher started", 2));

    @Flyway
    @ArquillianResource
    RdbmsPopulator flywayRdbmsPopulator;

    @DbUnit
    @ArquillianResource
    RdbmsPopulator dbUnitRdbmsPopulator;

    @Test
    void should_find_all_hollywood_films() {

        final URI jdbcUri = URI.create(
                String.format("jdbc:postgresql://%s:%d/%s", postgresql.getIpAddress(), postgresql.getBindPort(5432),
                        DB));

        flywayRdbmsPopulator
                .forUri(jdbcUri)
                .withDriver(Driver.class)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .usingDataSet("db/migration")
                .execute();

        dbUnitRdbmsPopulator.forUri(jdbcUri)
                .withDriver(Driver.class)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .usingDataSet("hollywoodfilms.yml")
                .withOptions(options()
                        .schema("hollywood")
                        .build())
                .execute();

        final Table table = new Table(new Source(jdbcUri.toString() + "?currentSchema=hollywood", USERNAME, PASSWORD),
                "films");
        assertThat(table).column("title")
                .value().isEqualTo("Trolls");

        flywayRdbmsPopulator
                .forUri(jdbcUri)
                .withDriver(Driver.class)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .usingDataSet("db/migration")
                .clean();

    }

}
