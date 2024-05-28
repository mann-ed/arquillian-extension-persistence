package org.arquillian.ape.rdbms.dbunit;

import static org.arquillian.ape.rdbms.dbunit.DbUnitOptions.options;
import static org.assertj.db.api.Assertions.assertThat;

import java.net.URI;

import org.arquillian.ape.junit.extension.ArquillianPersistenceExtension;
import org.arquillian.ape.rdbms.core.RdbmsPopulator;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author Edward Mann <ed.mann@edmann.com>
 *         <p>
 *         Created: May 27, 2024
 */
@ExtendWith(ArquillianPersistenceExtension.class)
@Disabled("Just used for testing can be deleted")
class PostgresqlUuidTest {

    @DbUnit
    @ArquillianResource
    RdbmsPopulator rdbmsPopulator;

    @BeforeAll
    public static void createSchema() throws Exception {
        final JdbcDatabaseTester jdbcTester = new JdbcDatabaseTester("org.postgresql.Driver",
                "jdbc:postgresql://localhost/dbunit",
                "dbunit", "dbunit");
        jdbcTester.getConnection().getConnection().prepareStatement("create table if not exists users (\n"
                + "    id uuid NOT NULL PRIMARY KEY,\n"
                + "    first_name varchar(33),\n"
                + "    last_name varchar(44),\n"
                + "    age  smallint\n"
                + ")").execute();
    }

    @Test
    void should_find_all_users() {
        rdbmsPopulator.forUri(URI.create("jdbc:postgresql://localhost/dbunit"))
                .withDriver(org.postgresql.Driver.class)
                .withUsername("dbunit")
                .withPassword("dbunit")
                .usingDataSet("users.xml")
                .withOptions(options()
                        .caseSensitiveTableNames(false).datatypeFactory(new PostgresqlDataTypeFactory())
                        .build())
                .execute();

        final Table table = new Table(new Source("jdbc:postgresql://localhost/dbunit", "dbunit", "dbunit"), "users");
        assertThat(table).column("first_name")
                .value().isEqualTo("Bob");

        rdbmsPopulator.forUri(URI.create("jdbc:postgresql://localhost/dbunit"))
                .withDriver(org.postgresql.Driver.class)
                .withUsername("dbunit")
                .withPassword("dbunit").withOptions(options()
                        .caseSensitiveTableNames(false).datatypeFactory(new PostgresqlDataTypeFactory())
                        .build())
                .usingDataSet("users.xml")
                .clean();
    }

}
