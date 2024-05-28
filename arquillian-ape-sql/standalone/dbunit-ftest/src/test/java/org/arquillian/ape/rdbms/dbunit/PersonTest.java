package org.arquillian.ape.rdbms.dbunit;

import static org.arquillian.ape.rdbms.dbunit.DbUnitOptions.options;
import static org.assertj.db.api.Assertions.assertThat;

import java.net.URI;
import java.nio.charset.Charset;
import java.sql.SQLException;

import org.arquillian.ape.junit.extension.ArquillianPersistenceExtension;
import org.arquillian.ape.rdbms.core.RdbmsPopulator;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.h2.Driver;
import org.h2.tools.RunScript;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianPersistenceExtension.class)
public class PersonTest {

    @DbUnit
    @ArquillianResource
    RdbmsPopulator rdbmsPopulator;

    @BeforeAll
    public static void createSchema() throws SQLException {
        // H2 tool for creating schema

        RunScript.execute("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "sa", "", "src/test/resources/schema.sql", Charset.forName("UTF-8"), false);
    }

    @Test
    void should_find_all_persons() {
        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("person.xml")
                .withOptions(options()
                        .caseSensitiveTableNames(true)
                        .build())
                .execute();

        final Table table = new Table(new Source("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", ""), "person");
        assertThat(table).column("name")
                .value().isEqualTo("Bob")
                .value().isEqualTo("Alice")
                .value().isEqualTo("Charlie");

        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("person.xml")
                .clean();
    }

    @Test
    void should_find_all_heroes() {
        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("heroes.yml")
                .execute();

        final Table table = new Table(new Source("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", ""), "person");
        assertThat(table).column("name")
                .value().isEqualTo("Clark")
                .value().isEqualTo("Lex");

        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("heroes.yml")
                .clean();
    }

    @Test
    void should_find_all_captains() {
        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("captains.json")
                .execute();

        final Table table = new Table(new Source("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", ""), "person");
        assertThat(table).column("name")
                .value().isEqualTo("Jean-Luc")
                .value().isEqualTo("James");

        rdbmsPopulator.forUri(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
                .withDriver(Driver.class)
                .withUsername("sa")
                .withPassword("")
                .usingDataSet("captains.json")
                .clean();
    }
}
