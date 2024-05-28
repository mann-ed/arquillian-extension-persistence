package org.arquillian.ape.nosql.couchbase;

import static org.assertj.core.api.Assertions.assertThat;

import org.arquillian.ape.junit.extension.ArquillianPersistenceExtension;
import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.docker.impl.requirement.RequiresDockerMachine;
import org.arquillian.cube.docker.junit5.ContainerDsl;
import org.arquillian.cube.docker.junit5.ContainerDslResolver;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

@ExtendWith(ContainerDslResolver.class)
@ExtendWith(ArquillianPersistenceExtension.class)
@RequiresDockerMachine(name = "dev")
@Disabled("arquillian.cube needs to update to Jakarta")
class CouchbaseTest {

    private ContainerDsl redis = new ContainerDsl("couchbase:travel")
            .withPortBinding(6379);

    @ArquillianResource
    @Couchbase
    NoSqlPopulator populator;
    @HostIp
    private String hostIp;

    @Test
    void should_find_books() {
        populator.forServer(hostIp, 0)
                .withStorage("travel-sample")
                .usingDataSet("airlines.json")
                .execute();

        final CouchbaseCluster couchbaseCluster = CouchbaseCluster.create(hostIp);
        final Bucket           books            = couchbaseCluster.openBucket("travel-sample");
        final JsonDocument     vueling          = books.get("airline_1");
        final JsonObject       vuelingObject    = vueling.content();

        assertThat(vuelingObject.getString("name")).isEqualTo("Vueling Airlines");
    }
}
