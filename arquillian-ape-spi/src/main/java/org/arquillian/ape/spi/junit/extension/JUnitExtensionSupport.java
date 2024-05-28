package org.arquillian.ape.spi.junit.extension;

import java.lang.annotation.Annotation;

import org.arquillian.ape.api.DeclarativeSupport;
import org.arquillian.ape.spi.Populator;
import org.arquillian.ape.spi.PopulatorService;

/**
 * Java SPI for registering a populator to be able to be used as JUnit
 * Extension.
 */
public interface JUnitExtensionSupport {

    Class<? extends Annotation> populatorAnnotation();

    Class<? extends PopulatorService<?>> populatotService();

    Class<? extends Populator<?, ?>> populator();

    DeclarativeSupport declarativeSupport();

}
