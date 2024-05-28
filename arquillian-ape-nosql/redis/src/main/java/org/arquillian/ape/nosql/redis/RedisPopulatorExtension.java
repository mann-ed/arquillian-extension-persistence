package org.arquillian.ape.nosql.redis;

import java.lang.annotation.Annotation;

import org.arquillian.ape.api.DeclarativeSupport;
import org.arquillian.ape.nosql.NoSqlPopulator;
import org.arquillian.ape.nosql.NoSqlPopulatorEnricher;
import org.arquillian.ape.spi.Populator;
import org.arquillian.ape.spi.PopulatorService;
import org.arquillian.ape.spi.junit.extension.JUnitExtensionSupport;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class RedisPopulatorExtension implements LoadableExtension, JUnitExtensionSupport {

    @Override
    public void register(final ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(PopulatorService.class, RedisPopulatorService.class)
                .service(ResourceProvider.class, NoSqlPopulatorEnricher.class)
                .observer(RedisDeclarativeSupport.class);
    }

    @Override
    public Class<? extends Annotation> populatorAnnotation() {
        return Redis.class;
    }

    @Override
    public Class<? extends PopulatorService<?>> populatotService() {
        return RedisPopulatorService.class;
    }

    @Override
    public Class<? extends Populator<?, ?>> populator() {
        return NoSqlPopulator.class;
    }

    @Override
    public DeclarativeSupport declarativeSupport() {
        return new RedisDeclarativeSupport();
    }
}
