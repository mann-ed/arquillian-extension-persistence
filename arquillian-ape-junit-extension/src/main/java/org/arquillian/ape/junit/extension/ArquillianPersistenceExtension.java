package org.arquillian.ape.junit.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.arquillian.ape.spi.Populator;
import org.arquillian.ape.spi.PopulatorService;
import org.arquillian.ape.spi.junit.extension.JUnitExtensionSupport;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ArquillianPersistenceExtension implements BeforeAllCallback, BeforeTestExecutionCallback {

    private static final Map<Class<? extends Annotation>, PopulatorInfo> populators = new HashMap<>();

    static {

        final ServiceLoader<JUnitExtensionSupport> serviceLoader = ServiceLoader.load(JUnitExtensionSupport.class);
        StreamSupport.stream(serviceLoader.spliterator(), false)
                .forEach(service -> {
                    populators.put(service.populatorAnnotation(),
                            new PopulatorInfo(service.populatotService(), service.populator()));
                });

    }

    static class PopulatorInfo {
        Class<? extends PopulatorService<?>> populatorService;
        Class<? extends Populator<?, ?>>     populator;

        PopulatorInfo(final Class<? extends PopulatorService<?>> class1,
                final Class<? extends Populator<?, ?>> class2) {
            this.populatorService = class1;
            this.populator = class2;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.junit.jupiter.api.extension.BeforeAllCallback#beforeAll(org.junit.jupiter
     * .api.extension.ExtensionContext)
     */
    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        final List<Field> allFieldsAnnotatedWith = Reflection.getAllFieldsAnnotatedWith(context.getRequiredTestClass(),
                ArquillianResource.class);

        final Set<Map.Entry<Class<? extends Annotation>, PopulatorInfo>> entries = populators.entrySet();

        for (final Map.Entry<Class<? extends Annotation>, PopulatorInfo> serviceEntry : entries) {
            final Optional<Field> fieldAnnotedWithPopulatorAnnotation = Reflection
                    .getFieldAnnotedWith(allFieldsAnnotatedWith, serviceEntry.getKey());

            if (fieldAnnotedWithPopulatorAnnotation.isPresent()) {
                /*
                 * Reflection.instantiateServiceAndPopulatorAndInject(context.getTestClass().get
                 * (),
                 * fieldAnnotedWithPopulatorAnnotation.get(),
                 * serviceEntry.getValue().populatorService, serviceEntry.getValue().populator);
                 */
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.jupiter.api.extension.BeforeTestExecutionCallback#
     * beforeTestExecution(org.junit.jupiter.api.extension.ExtensionContext)
     */
    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        final List<Field> allFieldsAnnotatedWith = Reflection.getAllFieldsAnnotatedWith(context.getRequiredTestClass(),
                ArquillianResource.class);

        final Set<Map.Entry<Class<? extends Annotation>, PopulatorInfo>> entries = populators.entrySet();

        for (final Map.Entry<Class<? extends Annotation>, PopulatorInfo> serviceEntry : entries) {
            final Optional<Field> fieldAnnotedWithPopulatorAnnotation = Reflection
                    .getFieldAnnotedWith(allFieldsAnnotatedWith, serviceEntry.getKey());

            if (fieldAnnotedWithPopulatorAnnotation.isPresent()) {

                Reflection.instantiateServiceAndPopulatorAndInject(context.getRequiredTestInstance(),
                        fieldAnnotedWithPopulatorAnnotation.get(),
                        serviceEntry.getValue().populatorService, serviceEntry.getValue().populator);

            }
        }
    }

}
