package org.arquillian.ape.nosql.infinispan;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;

import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.Container;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.DockerContainer;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
@Disabled("arquillian.cube needs to update to jakarta")
class InfinispanTest {

    @Infinispan
    @ArquillianResource
    NoSqlPopulator populator;

    @DockerContainer
    Container infinispan = Container.withContainerName("infinispan")
            .fromImage("jboss/infinispan-server:9.0.0.Final")
            .withCommand("standalone")
            .withPortBinding(11222)
            .build();

    @Test
    void should_populate_infinispan() throws MalformedURLException {
        populator.forServer(infinispan.getIpAddress(), infinispan.getBindPort(11222))
                .usingDataSet("users.json")
                .execute();

        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addServer().host(infinispan.getIpAddress()).port(infinispan.getBindPort(11222));

        final RemoteCacheManager        remoteCacheManager = new RemoteCacheManager(configurationBuilder.build());
        final RemoteCache<Object, User> cache              = remoteCacheManager.getCache();

        assertThat(cache.get("alex")).isNotNull();

    }

}
