package org.arquillian.ape.junit.rule;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.arquillian.ape.api.DeclarativeSupport;
import org.arquillian.ape.spi.junit.extension.JUnitExtensionSupport;
import org.jboss.arquillian.test.spi.TestClass;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class DeclarativeArquillianPersistenceRule implements MethodRule {

    private final static Map<Class<? extends Annotation>, DeclarativeSupport> populators = new HashMap<>();

    static {

        final ServiceLoader<JUnitExtensionSupport> serviceLoader = ServiceLoader.load(JUnitExtensionSupport.class);
        StreamSupport.stream(serviceLoader.spliterator(), false)
                .forEach(service -> {
                    populators.put(service.populatorAnnotation(), service.declarativeSupport());
                });
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                final TestClass testClass = new TestClass(target.getClass());
                run(declarativeSupport -> declarativeSupport.configure(testClass));

                run(declarativeSupport -> declarativeSupport.clean(testClass, method.getMethod(), false));
                run(declarativeSupport -> declarativeSupport.populate(testClass, method.getMethod()));

                base.evaluate();

                run(declarativeSupport -> declarativeSupport.clean(testClass, method.getMethod(), true));

            }

            private void run(final Consumer<DeclarativeSupport> consumer) {
                final Collection<DeclarativeSupport> values = populators.values();

                for (final DeclarativeSupport declarativeSupport : values) {
                    consumer.accept(declarativeSupport);
                }

            }

        };
    }
}
