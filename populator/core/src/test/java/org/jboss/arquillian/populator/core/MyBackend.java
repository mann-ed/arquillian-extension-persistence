package org.jboss.arquillian.populator.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@org.jboss.arquillian.populator.api.Populator
public @interface MyBackend {
}
