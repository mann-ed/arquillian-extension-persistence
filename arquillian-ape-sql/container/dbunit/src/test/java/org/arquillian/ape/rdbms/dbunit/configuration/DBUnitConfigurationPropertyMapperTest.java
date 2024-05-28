package org.arquillian.ape.rdbms.dbunit.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

import java.util.Map;
import java.util.stream.Stream;

import org.arquillian.ape.rdbms.core.configuration.ConfigurationImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
class DBUnitConfigurationPropertyMapperTest {

    private static final String DBUNIT_FEATURES = "http://www.dbunit.org/features/";

    private static final String DBUNIT_PROPERTIES = "http://www.dbunit.org/properties/";

    private final DBUnitConfiguration configuration = new DBUnitConfiguration();

    private final ConfigurationImporter<DBUnitConfiguration> configurationImporter = new ConfigurationImporter<DBUnitConfiguration>(
            configuration);

    @ParameterizedTest
    @MethodSource("expectedDBUnitProperties")
    void should_convert_internal_configuration_representation_to_fully_qualified_dbunit_configuration_set(
            final String expectedKey, final Object expectedValue) throws Exception {
        // given
        configurationImporter.loadFromArquillianXml("arquillian-dbunit.xml");
        final DBUnitConfigurationPropertyMapper dbUnitConfigurationPropertyMapper = new DBUnitConfigurationPropertyMapper();

        // when
        final Map<String, Object> dbunitProperties = dbUnitConfigurationPropertyMapper.map(configuration);

        // then
        assertThat(dbunitProperties).contains(entry(expectedKey, expectedValue));
    }

    @Test
    void should_convert_internal_configuration_representation_of_table_type_to_fully_qualified_dbunit_configuration_set()
            throws Exception {
        // given
        configurationImporter.loadFromArquillianXml("arquillian-dbunit.xml");
        final DBUnitConfigurationPropertyMapper dbUnitConfigurationPropertyMapper = new DBUnitConfigurationPropertyMapper();

        // when
        final Map<String, Object> dbunitProperties = dbUnitConfigurationPropertyMapper.map(configuration);
        final String[]            actualTableType  = (String[]) dbunitProperties.get(DBUNIT_PROPERTIES + "tableType");

        // then
        assertThat(actualTableType).containsOnly("TABLE", "VIEW");
    }

    @Test
    void should_convert_non_null_values_only() throws Exception {
        // given
        configurationImporter.loadFromArquillianXml("arquillian-dbunit-batchsize-only.xml");
        final DBUnitConfigurationPropertyMapper dbUnitConfigurationPropertyMapper = new DBUnitConfigurationPropertyMapper();

        // when
        final Map<String, Object> dbunitProperties = dbUnitConfigurationPropertyMapper.map(configuration);

        // then
        assertThat(dbunitProperties).contains(entry(DBUNIT_PROPERTIES + "batchSize", 200));
    }

    @Test
    void should_not_overwrite_when_null_specified() throws Exception {
        // given
        configurationImporter.loadFromArquillianXml("arquillian-dbunit-batchsize-only.xml");
        final DBUnitConfigurationPropertyMapper dbUnitConfigurationPropertyMapper = new DBUnitConfigurationPropertyMapper();

        // when
        final Map<String, Object> dbunitProperties = dbUnitConfigurationPropertyMapper.map(configuration);

        // then
        assertThat(dbunitProperties).contains(entry(DBUNIT_PROPERTIES + "fetchSize", 100));
    }

    private static Stream<Arguments> expectedDBUnitProperties() {
        return Stream.of(
                Arguments.of(DBUNIT_FEATURES + "batchedStatements", true),
                Arguments.of(DBUNIT_FEATURES + "caseSensitiveTableNames", true),
                Arguments.of(DBUNIT_FEATURES + "qualifiedTableNames", true),
                Arguments.of(DBUNIT_FEATURES + "datatypeWarning", false),
                Arguments.of(DBUNIT_FEATURES + "skipOracleRecycleBinTables", true),
                Arguments.of(DBUNIT_FEATURES + "allowEmptyFields", true),
                Arguments.of(DBUNIT_PROPERTIES + "escapePattern", "?"),
                Arguments.of(DBUNIT_PROPERTIES + "batchSize", 200),
                Arguments.of(DBUNIT_PROPERTIES + "fetchSize", 300));
    }
}
