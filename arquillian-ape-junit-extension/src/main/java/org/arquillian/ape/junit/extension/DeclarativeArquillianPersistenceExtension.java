package org.arquillian.ape.junit.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.arquillian.ape.api.DeclarativeSupport;
import org.arquillian.ape.spi.junit.extension.JUnitExtensionSupport;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DeclarativeArquillianPersistenceExtension
        implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Map<Class<? extends Annotation>, DeclarativeSupport> populators = new HashMap<>();

    static {

        final ServiceLoader<JUnitExtensionSupport> serviceLoader = ServiceLoader.load(JUnitExtensionSupport.class);
        StreamSupport.stream(serviceLoader.spliterator(), false)
                .forEach(service -> {
                    populators.put(service.populatorAnnotation(), service.declarativeSupport());
                });
    }

    private void run(final Consumer<DeclarativeSupport> consumer) {
        final Collection<DeclarativeSupport> values = populators.values();

        for (final DeclarativeSupport declarativeSupport : values) {
            consumer.accept(declarativeSupport);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.junit.jupiter.api.extension.AfterTestExecutionCallback#afterTestExecution
     * (org.junit.jupiter.api.extension.ExtensionContext)
     */
    @Override
    public void afterTestExecution(final ExtensionContext context) throws Exception {
        final TestClass testClass = new TestClass(context.getRequiredTestClass());
        final Method    method    = context.getRequiredTestMethod();
        run(declarativeSupport -> declarativeSupport.clean(testClass, method, true));

    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.jupiter.api.extension.BeforeTestExecutionCallback#
     * beforeTestExecution(org.junit.jupiter.api.extension.ExtensionContext)
     */
    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        final TestClass testClass = new TestClass(context.getRequiredTestClass());
        System.out.println(testClass.getClass().getCanonicalName());
        final Method method = context.getRequiredTestMethod();
        run(declarativeSupport -> declarativeSupport.configure(testClass));

        run(declarativeSupport -> declarativeSupport.clean(testClass, method,
                false));
        run(declarativeSupport -> declarativeSupport.populate(testClass, method));

    }
}
