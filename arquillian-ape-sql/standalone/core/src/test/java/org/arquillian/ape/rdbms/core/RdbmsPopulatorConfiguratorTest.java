package org.arquillian.ape.rdbms.core;

import static org.mockito.Mockito.times;

import java.net.URI;
import java.util.HashMap;

import org.h2.Driver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RdbmsPopulatorConfiguratorTest {

    @Mock
    RdbmsPopulatorService<?> rdbmsPopulatorService;

    @Test
    void should_load_spring_boot_properties_from_default_location() {

        // given
        final RdbmsPopulatorConfigurator rdbmsPopulatorConfigurator = new RdbmsPopulatorConfigurator(null,
                rdbmsPopulatorService);

        // when
        rdbmsPopulatorConfigurator.fromSpringBootConfiguration().execute();

        // then
        Mockito.verify(rdbmsPopulatorService, times(1))
                .connect(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), "sa", "sa",
                        Driver.class,
                        new HashMap<>());
    }

    @Test
    void should_load_jpa_persistence_from_default_location() {

        // given
        final RdbmsPopulatorConfigurator rdbmsPopulatorConfigurator = new RdbmsPopulatorConfigurator(null,
                rdbmsPopulatorService);

        // when
        rdbmsPopulatorConfigurator.fromJpaPersistence().execute();

        // then
        Mockito.verify(rdbmsPopulatorService, times(1))
                .connect(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), "sa", "sa",
                        Driver.class,
                        new HashMap<>());
    }

    @Test
    void should_load_wildfly_swarm_from_default_location() {

        // given
        final RdbmsPopulatorConfigurator rdbmsPopulatorConfigurator = new RdbmsPopulatorConfigurator(null,
                rdbmsPopulatorService);

        // when
        rdbmsPopulatorConfigurator.fromWildflySwarmConfiguration().execute();

        // then
        Mockito.verify(rdbmsPopulatorService, times(1))
                .connect(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), "sa", "sa",
                        org.h2.Driver.class,
                        new HashMap<>());
    }

    @Test
    void should_load_wildfly_swarm_from_default_location_and_concrete_name() {

        // given
        final RdbmsPopulatorConfigurator rdbmsPopulatorConfigurator = new RdbmsPopulatorConfigurator(null,
                rdbmsPopulatorService);

        // when
        rdbmsPopulatorConfigurator.fromWildflySwarmConfiguration("MyDS").execute();

        // then
        Mockito.verify(rdbmsPopulatorService, times(1))
                .connect(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), "sa", "sa",
                        org.h2.Driver.class,
                        new HashMap<>());
    }

    @Test
    void should_load_wildfly_swarm_from_specific_location_and_autoresolution_of_driver() {

        // given
        final RdbmsPopulatorConfigurator rdbmsPopulatorConfigurator = new RdbmsPopulatorConfigurator(null,
                rdbmsPopulatorService);

        // when
        rdbmsPopulatorConfigurator.fromWildflySwarmConfiguration("MyDS", "custom-project-defaults.yml").execute();

        // then
        Mockito.verify(rdbmsPopulatorService, times(1))
                .connect(URI.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"), "sa", "sa",
                        org.h2.Driver.class,
                        new HashMap<>());
    }

}
