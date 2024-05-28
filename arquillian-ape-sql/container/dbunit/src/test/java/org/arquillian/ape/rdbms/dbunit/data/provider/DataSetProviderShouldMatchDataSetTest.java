/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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
package org.arquillian.ape.rdbms.dbunit.data.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.arquillian.ape.api.UsingDataSet;
import org.arquillian.ape.rdbms.ShouldMatchDataSet;
import org.arquillian.ape.rdbms.core.dbunit.data.descriptor.Format;
import org.arquillian.ape.rdbms.core.exception.InvalidResourceLocation;
import org.arquillian.ape.rdbms.core.exception.UnsupportedDataFormatException;
import org.arquillian.ape.rdbms.core.metadata.DbUnitMetadataExtractor;
import org.arquillian.ape.rdbms.dbunit.configuration.DBUnitConfiguration;
import org.arquillian.ape.rdbms.dbunit.data.descriptor.DataSetResourceDescriptor;
import org.arquillian.ape.testutils.DataSetDescriptorAssert;
import org.arquillian.ape.testutils.TestConfigurationLoader;
import org.jboss.arquillian.test.spi.event.suite.TestEvent;
import org.junit.jupiter.api.Test;

class DataSetProviderShouldMatchDataSetTest {

    private static final String DEFAULT_FILENAME_FOR_TEST_METHOD = "expected-"
            + ShouldMatchDataSetAnnotatedClass.class.getName() + "#shouldPassWithDataFileNotSpecified.xls";

    private static final String XML_EXPECTED_DATA_SET_ON_CLASS_LEVEL = "datasets/xml/expected-class-level.xml";

    private static final String XML_EXPECTED_DATA_SET_ON_METHOD_LEVEL = "datasets/xml/expected-method-level.xml";

    private static final String EXCEL_EXPECTED_DATA_SET_ON_METHOD_LEVEL = "datasets/xls/expected-method-level.xls";

    private DBUnitConfiguration defaultConfiguration = TestConfigurationLoader.createDefaultDBUnitConfiguration();

    private static TestEvent createTestEvent(final String testMethod) throws NoSuchMethodException {
        final TestEvent testEvent = new TestEvent(new ShouldMatchDataSetAnnotatedClass(),
                ShouldMatchDataSetAnnotatedClass.class.getMethod(testMethod));
        return testEvent;
    }

    @Test
    void should_fetch_all_expected_data_sets_defined_for_test_class() throws Exception {
        // given
        final TestEvent               testEvent       = createTestEvent(
                "shouldPassWithDataButWithoutFormatDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final Collection<DataSetResourceDescriptor> dataSetDescriptors = dataSetProvider
                .getDescriptors(testEvent.getTestClass());

        // then
        DataSetDescriptorAssert.assertThat(dataSetDescriptors)
                .containsOnlyFollowingFiles(XML_EXPECTED_DATA_SET_ON_CLASS_LEVEL,
                        XML_EXPECTED_DATA_SET_ON_METHOD_LEVEL, DEFAULT_FILENAME_FOR_TEST_METHOD,
                        EXCEL_EXPECTED_DATA_SET_ON_METHOD_LEVEL, "one.xml", "two.xls", "three.yml");
    }

    @Test
    void should_fetch_data_file_name_from_test_level_annotation() throws Exception {
        // given
        final String                  expectedDataFile = XML_EXPECTED_DATA_SET_ON_METHOD_LEVEL;
        final TestEvent               testEvent        = createTestEvent(
                "shouldPassWithDataButWithoutFormatDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider  = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<String> dataFiles = new ArrayList<String>(
                dataSetProvider.getResourceFileNames(testEvent.getTestMethod()));

        // then
        assertThat(dataFiles).containsOnly(expectedDataFile);
    }

    @Test
    void should_fetch_data_from_class_level_annotation_when_not_defined_for_test_method() throws Exception {
        // given
        final String                  expectedDataFile = XML_EXPECTED_DATA_SET_ON_METHOD_LEVEL;
        final TestEvent               testEvent        = createTestEvent(
                "shouldPassWithDataButWithoutFormatDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider  = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<String> dataFiles = new ArrayList<String>(
                dataSetProvider.getResourceFileNames(testEvent.getTestMethod()));

        // then
        assertThat(dataFiles).containsOnly(expectedDataFile);
    }

    @Test
    void should_fetch_data_format_from_method_level_annotation() throws Exception {
        // given
        final Format                  expectedFormat  = Format.EXCEL;
        final TestEvent               testEvent       = createTestEvent(
                "shouldPassWithDataAndFormatDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<Format> dataFormats = dataSetProvider.getDataFormats(testEvent.getTestMethod());

        // then
        assertThat(dataFormats).containsOnly(expectedFormat);
    }

    @Test
    void should_infer_data_format_from_file_name_when_not_defined_on_method_level_annotation() throws Exception {
        // given
        final Format                  expectedFormat  = Format.XML;
        final TestEvent               testEvent       = createTestEvent(
                "shouldPassWithDataButWithoutFormatDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<Format> dataFormats = dataSetProvider.getDataFormats(testEvent.getTestMethod());

        // then
        assertThat(dataFormats).containsOnly(expectedFormat);
    }

    @Test
    void should_infer_data_format_from_file_name_when_not_defined_on_class_level_annotation() throws Exception {
        // given
        final Format                  expectedFormat  = Format.XML;
        final TestEvent               testEvent       = createTestEvent("shouldPassWithoutDataDefinedOnMethodLevel");
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<Format> dataFormats = dataSetProvider.getDataFormats(testEvent.getTestMethod());

        // then
        assertThat(dataFormats).containsOnly(expectedFormat);
    }

    @Test
    void should_throw_exception_when_format_cannot_be_infered_from_file_extension() throws Exception {
        // given
        final TestEvent               testEvent       = new TestEvent(
                new ShouldMatchDataSetAnnotationWithUnsupportedFormat(),
                ShouldMatchDataSetAnnotationWithUnsupportedFormat.class
                        .getMethod("shouldFailWithNonSupportedFileExtension"));
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        assertThrows(UnsupportedDataFormatException.class, () -> {
            new ArrayList<Format>(
                    dataSetProvider.getDataFormats(testEvent.getTestMethod()));
        });

        // then
        // exception should be thrown
    }

    @Test
    void should_provide_default_file_name_when_not_specified_in_annotation() throws Exception {
        // given
        final String                  expectedFileName = DEFAULT_FILENAME_FOR_TEST_METHOD;
        final TestEvent               testEvent        = createTestEvent("shouldPassWithDataFileNotSpecified");
        final ExpectedDataSetProvider dataSetProvider  = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<String> files = new ArrayList<String>(
                dataSetProvider.getResourceFileNames(testEvent.getTestMethod()));

        // then
        assertThat(files).containsOnly(expectedFileName);
    }

    @Test
    void should_provide_default_file_name_when_not_specified_in_annotation_on_class_level() throws Exception {
        // given
        final String                  expectedFileName = "expected-"
                + ShouldMatchDataSetAnnotatedOnClassLevelOnly.class.getName() + ".xls";
        final TestEvent               testEvent        = new TestEvent(
                new ShouldMatchDataSetAnnotatedOnClassLevelOnly(),
                ShouldMatchDataSetAnnotatedOnClassLevelOnly.class.getMethod("shouldPass"));
        final ExpectedDataSetProvider dataSetProvider  = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final Collection<DataSetResourceDescriptor> files = dataSetProvider.getDescriptors(testEvent.getTestClass());

        // then
        assertThat(files).containsOnly(new DataSetResourceDescriptor(expectedFileName, Format.EXCEL));
    }

    @Test
    void should_extract_all_data_set_files() throws Exception {
        // given
        final DataSetResourceDescriptor xml             = new DataSetResourceDescriptor("one.xml", Format.XML);
        final DataSetResourceDescriptor xls             = new DataSetResourceDescriptor("two.xls", Format.EXCEL);
        final DataSetResourceDescriptor yml             = new DataSetResourceDescriptor("three.yml", Format.YAML);
        final TestEvent                 testEvent       = new TestEvent(new ShouldMatchDataSetAnnotatedClass(),
                ShouldMatchDataSetAnnotatedClass.class.getMethod("shouldPassWithMultipleFilesDefined"));
        final ExpectedDataSetProvider   dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<DataSetResourceDescriptor> dataSetDescriptors = new ArrayList<DataSetResourceDescriptor>(
                dataSetProvider.getDescriptorsDefinedFor(testEvent.getTestMethod()));

        // then
        assertThat(dataSetDescriptors).containsExactly(xml, xls, yml);
    }

    @Test
    void should_throw_exception_for_non_existing_file_infered_from_class_level_annotation() throws Exception {
        // given
        final TestEvent               testEvent       = new TestEvent(
                new ShouldMatchDataSetAnnotatedOnClassLevelOnlyNonExistingFile(),
                ShouldMatchDataSetAnnotatedOnClassLevelOnlyNonExistingFile.class.getMethod("shouldFail"));
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        assertThrows(InvalidResourceLocation.class, () -> {
            dataSetProvider
                    .getDescriptorsDefinedFor(testEvent.getTestMethod());
        });

        // then
        // exception should be thrown
    }

    @Test
    void should_throw_exception_for_non_existing_file_defined_on_method_level_annotation() throws Exception {
        // given
        final TestEvent               testEvent       = new TestEvent(
                new ShouldMatchDataSetOnTestMethodLevelWithNonExistingFileAndDefaultLocation(),
                ShouldMatchDataSetOnTestMethodLevelWithNonExistingFileAndDefaultLocation.class.getMethod(
                        "shouldFailForNonExistingFile"));
        final ExpectedDataSetProvider dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        assertThrows(InvalidResourceLocation.class, () -> {
            dataSetProvider
                    .getDescriptorsDefinedFor(testEvent.getTestMethod());
        });

        // then
        // exception should be thrown
    }

    // ----------------------------------------------------------------------------------------

    @Test
    void should_find_file_in_default_location_if_not_specified_explicitly() throws Exception {
        // given
        final DataSetResourceDescriptor expectedFile    = new DataSetResourceDescriptor(
                defaultConfiguration.getDefaultDataSetLocation() + "/tables-in-datasets-folder.yml", Format.YAML);
        final TestEvent                 testEvent       = new TestEvent(
                new ShouldMatchDataSetOnTestMethodLevelWithNonExistingFileAndDefaultLocation(),
                ShouldMatchDataSetOnTestMethodLevelWithNonExistingFileAndDefaultLocation.class.getMethod(
                        "shouldPassForFileStoredInDefaultLocation"));
        final ExpectedDataSetProvider   dataSetProvider = new ExpectedDataSetProvider(
                new DbUnitMetadataExtractor(testEvent.getTestClass()), defaultConfiguration);

        // when
        final List<DataSetResourceDescriptor> dataSetDescriptors = new ArrayList<DataSetResourceDescriptor>(
                dataSetProvider.getDescriptorsDefinedFor(testEvent.getTestMethod()));

        // then
        assertThat(dataSetDescriptors).containsOnly(expectedFile);
    }

    @UsingDataSet("datasets/test.xml")
    @ShouldMatchDataSet(XML_EXPECTED_DATA_SET_ON_CLASS_LEVEL)
    private static class ShouldMatchDataSetAnnotatedClass {
        public void shouldPassWithoutDataDefinedOnMethodLevel() {
        }

        @ShouldMatchDataSet(XML_EXPECTED_DATA_SET_ON_METHOD_LEVEL)
        public void shouldPassWithDataButWithoutFormatDefinedOnMethodLevel() {
        }

        @ShouldMatchDataSet(value = EXCEL_EXPECTED_DATA_SET_ON_METHOD_LEVEL)
        public void shouldPassWithDataAndFormatDefinedOnMethodLevel() {
        }

        @ShouldMatchDataSet
        public void shouldPassWithDataFileNotSpecified() {
        }

        @ShouldMatchDataSet({
                "one.xml", "two.xls", "three.yml"
        })
        public void shouldPassWithMultipleFilesDefined() {
        }
    }

    private static class ShouldMatchDataSetAnnotationWithUnsupportedFormat {
        @ShouldMatchDataSet("arquillian.ike")
        public void shouldFailWithNonSupportedFileExtension() {
        }
    }

    @ShouldMatchDataSet
    private static class ShouldMatchDataSetAnnotatedOnClassLevelOnly {
        public void shouldPass() {
        }
    }

    @ShouldMatchDataSet
    private static class ShouldMatchDataSetAnnotatedOnClassLevelOnlyNonExistingFile {
        public void shouldFail() {
        }
    }

    private static class ShouldMatchDataSetOnTestMethodLevelWithNonExistingFileAndDefaultLocation {
        @ShouldMatchDataSet("non-existing.xml")
        public void shouldFailForNonExistingFile() {
        }

        @ShouldMatchDataSet("tables-in-datasets-folder.yml")
        public void shouldPassForFileStoredInDefaultLocation() {
        }
    }
}
