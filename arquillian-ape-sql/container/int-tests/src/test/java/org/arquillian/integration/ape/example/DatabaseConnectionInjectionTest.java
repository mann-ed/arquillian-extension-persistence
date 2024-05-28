/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.integration.ape.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.arquillian.ape.rdbms.PersistenceTest;
import org.arquillian.integration.ape.util.Query;
import org.dbunit.database.DatabaseConnection;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:bartosz.majsak@gmail.com">Bartosz Majsak</a>
 */
@ExtendWith(ArquillianExtension.class)
@PersistenceTest
public class DatabaseConnectionInjectionTest {

    // Test needs to be "persistence-extension-aware" in order to get this
    // reference.
    // This can be achieved using any of APE annotations such as
    // @PersistenceTest, @UsingDataSet, @ShouldMatchDataSet etc.
    @ArquillianResource
    private DatabaseConnection databaseConnection;

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(UserAccount.class.getPackage())
                .addClass(Query.class)
                // required for remote containers in order to run tests with FEST-Asserts
                .addPackages(true, "org.assertj.core")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    void should_inject_dbunit_database_connection() throws Exception {
        assertThat(databaseConnection).isNotNull();
    }
}
