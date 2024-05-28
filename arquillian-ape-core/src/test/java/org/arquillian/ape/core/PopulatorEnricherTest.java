package org.arquillian.ape.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.arquillian.ape.spi.Populator;
import org.arquillian.ape.spi.PopulatorService;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PopulatorEnricherTest {

    @Mock
    ServiceLoader serviceLoader;

    @Mock
    Injector injector;

    @BeforeEach
    void setupMocks() {
        final PopulatorService populatorService = new TestPopulatorService();
        when(injector.inject(any())).thenAnswer(element -> element.getArgument(0));
        when(serviceLoader.all(PopulatorService.class)).thenReturn(Collections.singletonList(populatorService));
    }

    @Test
    void should_create_populator_with_configured_annotation() throws NoSuchFieldException {
        final MyPopulatorEnricher populatorEnricher = new MyPopulatorEnricher();
        populatorEnricher.serviceLoaderInstance = () -> serviceLoader;
        populatorEnricher.injectorInstance = () -> injector;

        final Object populator = populatorEnricher.lookup(null, (Annotation) () -> MyBackend.class);

        assertThat(populator).isInstanceOf(MyPopulator.class);
        assertThat(((Populator) populator).getPopulatorService()).isInstanceOf(TestPopulatorService.class);
    }

    static class TestPopulatorService implements PopulatorService<MyBackend> {

        @Override
        public Class<MyBackend> getPopulatorAnnotation() {
            return MyBackend.class;
        }
    }
}
