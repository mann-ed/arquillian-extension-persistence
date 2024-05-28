package org.arquillian.integration.ape.dsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.arquillian.ape.rdbms.PersistenceTest;
import org.arquillian.ape.rdbms.core.RdbmsPopulator;
import org.arquillian.ape.rdbms.core.configuration.PersistenceConfiguration;
import org.arquillian.ape.rdbms.dbunit.DbUnit;
import org.arquillian.ape.rdbms.dbunit.DbUnitOptions;
import org.arquillian.integration.ape.example.UserAccount;
import org.arquillian.integration.ape.example.deployments.UserPersistenceWarDeploymentTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ExtendWith(ArquillianExtension.class)
@PersistenceTest
public class ApeDslIncontainerTest {

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return UserPersistenceWarDeploymentTest.createDeploymentPackage()
                .addAsResource("datasets/single-user.xls")
                .addAsResource("datasets/single-user.xml")
                .addAsResource("datasets/single-user.yml");
    }

    @ArquillianResource
    private PersistenceConfiguration persistenceConfiguration;

    @PersistenceContext
    private EntityManager em;

    @ArquillianResource
    @DbUnit
    private RdbmsPopulator db;

    @Test
    void should_find_user_using_excel_dataset_and_data_source() throws Exception {
        // given
        db.forUri(persistenceConfiguration.getDefaultDataSource())
                .usingDataSet("datasets/single-user.xls")
                .execute();
        final String expectedUsername = "doovde";

        // when
        final UserAccount user = em.find(UserAccount.class, 1L);

        // then
        assertThat(user.getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    void should_have_timestamp_populated() throws Exception {
        // given
        db.forUri(persistenceConfiguration.getDefaultDataSource())
                .usingDataSet("datasets/single-user.yml")
                .execute();
        final Date expectedOpenDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse("2001-01-01 00:00:00");

        // when
        final UserAccount user = em.find(UserAccount.class, 1L);

        // then
        assertThat(user.getOpenDate()).isEqualTo(expectedOpenDate);
    }

    @Test
    void should_find_user_using_xml_dataset() throws Exception {
        // given
        db.forUri(persistenceConfiguration.getDefaultDataSource())
                .usingDataSet("datasets/single-user.xml")
                .execute();
        final String expectedUsername = "doovde";

        // when
        final UserAccount user = em.find(UserAccount.class, 1L);

        // then
        assertThat(user.getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    void should_find_user_using_yaml_dataset() throws Exception {
        // given
        db.forUri(persistenceConfiguration.getDefaultDataSource())
                .usingDataSet("datasets/single-user.yml")
                .withOptions(DbUnitOptions.options().caseSensitiveTableNames(true).build())
                .execute();
        final String expectedUsername = "doovde";

        // when
        final UserAccount user = em.find(UserAccount.class, 1L);

        // then
        assertThat(user.getUsername()).isEqualTo(expectedUsername);
        assertThat(user.getNickname()).isNull();
    }
}
