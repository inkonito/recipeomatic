package fr.ropiteaux.rom.core.guice;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class Slf4jTypeListener implements TypeListener {
	@Override
	public <I> void hear(TypeLiteral<I> typeLiteral,
			TypeEncounter<I> typeEncounter) {
		for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
			if (field.getType() == Logger.class
					&& field.isAnnotationPresent(InjectLogger.class)) {
				typeEncounter.register(new Slf4JMembersInjector<I>(field));
			}
		}
	}
}