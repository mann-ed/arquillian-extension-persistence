package org.arquillian.ape.junit.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.arquillian.ape.spi.Populator;
import org.arquillian.ape.spi.PopulatorService;
import org.junit.platform.commons.support.AnnotationSupport;

class Reflection {

    private Reflection() {
    }

    public static boolean isClassWithAnnotation(final Class<?> source,
            final Class<? extends Annotation> annotationClass) {
        return AnnotationSupport.isAnnotated(source, annotationClass);
    }

    public static final List<Field> getAllFieldsAnnotatedWith(final Class<?> clazz,
            final Class<? extends Annotation> annotation) {
        final List<Field> fields = new ArrayList<>();

        Class<?> current = clazz;
        while (current.getSuperclass() != null) {

            fields.addAll(Arrays.stream(current.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .filter(field -> field.isAnnotationPresent(annotation))
                    .toList());

            current = current.getSuperclass();
        }

        return fields;
    }

    public static final Optional<Field> getFieldAnnotedWith(final List<Field> fields,
            final Class<? extends Annotation> annotation) {
        return fields.stream()
                .filter(field -> field.isAnnotationPresent(annotation))
                .findFirst();
    }

    public static final void instantiateServiceAndPopulatorAndInject(final Object testInstance, final Field field,
            final Class<? extends PopulatorService<?>> serviceClass,
            final Class<? extends Populator<?, ?>> populatorClass)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException,
            NoSuchFieldException, SecurityException, ClassNotFoundException {

        final Constructor<?> serviceConstructor = serviceClass.getDeclaredConstructor();
        serviceConstructor.setAccessible(true);
        final Object service = serviceConstructor.newInstance();

        // Using getDeclaredConstructor(serviceClass) does not work. Services must
        // contain only one constructor, if not even the Arquillian runner fails in case
        // of APE
        final Constructor<?> populatorConstructor = populatorClass.getDeclaredConstructors()[0];
        populatorConstructor.setAccessible(true);
        final Populator populator = (Populator) populatorConstructor.newInstance(service);
        field.set(testInstance, populator);

    }

}
