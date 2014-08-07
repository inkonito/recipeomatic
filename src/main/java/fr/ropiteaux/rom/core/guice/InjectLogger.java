package fr.ropiteaux.rom.core.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Scope
public @interface InjectLogger {
}
