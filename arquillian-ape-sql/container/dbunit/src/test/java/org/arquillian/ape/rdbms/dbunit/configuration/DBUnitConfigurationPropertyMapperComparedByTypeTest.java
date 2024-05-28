package org.arquillian.ape.rdbms.dbunit.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;

import org.arquillian.ape.rdbms.core.configuration.ConfigurationImporter;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.statement.StatementFactory;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.netezza.NetezzaMetadataHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
class DBUnitConfigurationPropertyMapperComparedByTypeTest {
    private final DBUnitConfiguration configuration = new DBUnitConfiguration();

    private final ConfigurationImporter<DBUnitConfiguration> configurationImporter = new ConfigurationImporter<DBUnitConfiguration>(
            configuration);

    @ParameterizedTest
    @MethodSource("expectedDbunitPropertiesToBeComparedByType")
    void should_convert_internal_configuration_representation_to_fully_qualified_dbunit_configuration_set(
            final String expectedKey, final Class<?> expectedType) throws Exception {
        // given
        configurationImporter.loadFromArquillianXml("arquillian-dbunit.xml");
        final DBUnitConfigurationPropertyMapper dbUnitConfigurationPropertyMapper = new DBUnitConfigurationPropertyMapper();

        // when
        final Map<String, Object> dbunitProperties = dbUnitConfigurationPropertyMapper.map(configuration);

        // then
        assertThat(dbunitProperties.get(expectedKey)).isInstanceOf(expectedType);
    }

    private static Stream<Arguments> expectedDbunitPropertiesToBeComparedByType() {
        return Stream.of(
                Arguments.of("http://www.dbunit.org/properties/datatypeFactory", HsqldbDataTypeFactory.class),
                Arguments.of("http://www.dbunit.org/properties/statementFactory", StatementFactory.class),
                Arguments.of("http://www.dbunit.org/properties/resultSetTableFactory",
                        ForwardOnlyResultSetTableFactory.class),
                Arguments.of("http://www.dbunit.org/properties/primaryKeyFilter", DefaultColumnFilter.class),
                Arguments.of("http://www.dbunit.org/properties/mssql/identityColumnFilter",
                        DefaultColumnFilter.class),
                Arguments.of("http://www.dbunit.org/properties/metadataHandler", NetezzaMetadataHandler.class));
    }
}
