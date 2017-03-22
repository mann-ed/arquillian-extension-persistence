package org.jboss.arquillian.populator.nosql.api;

import org.jboss.arquillian.populator.core.Populator;

/**
 * NoSql Populator that creates the NoSqlPopulatorConfigurator with specific DSL methods related to NoSql engines.
 * @see NoSqlPopulatorConfigurator
 */
public class NoSqlPopulator extends Populator<NoSqlPopulatorService, NoSqlPopulatorConfigurator> {

    public NoSqlPopulator(NoSqlPopulatorService populatorService) {
        super(populatorService);
    }

    @Override
    public NoSqlPopulatorConfigurator createExecutor() {
        return new NoSqlPopulatorConfigurator(this.host, this.bindPort, this.populatorService);
    }

}
