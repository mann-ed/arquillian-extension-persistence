/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full Arrays.asListing of individual contributors.
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
package org.arquillian.ape.rdbms.dbunit;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
class DataSetUtilsTest {

    @ParameterizedTest
    @MethodSource("columns")
    void should_extract_non_existing_columns_defined_in_second_list(final List<String> expectedColumns,
            final List<String> actualColumns, final List<String> nonExistingColums) throws Exception {
        // when
        final List<String> actualNonExistingColumns = DataSetUtils.extractNonExistingColumns(expectedColumns,
                actualColumns);

        // then
        assertThat(actualNonExistingColumns).isEqualTo(nonExistingColums);
    }

    private static Stream<Arguments> columns() {
        return Stream.of(
                // expected , actual , non existing in actual
                Arguments.of(asList("id", "name"), asList("name", "password"), singletonList("id")),
                Arguments.of(asList("id", "username", "password"), asList("id", "username", "password"), emptyList()),
                Arguments.of(emptyList(), asList("id", "name"), emptyList()),
                Arguments.of(emptyList(), emptyList(), emptyList()));
    }
}
