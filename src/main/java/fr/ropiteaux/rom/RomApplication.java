package fr.ropiteaux.rom;

import fr.ropiteaux.rom.core.guice.Slf4jTypeListener;
import fr.ropiteaux.rom.core.manager.PersistenceManager;
import fr.ropiteaux.rom.core.security.RomSecurityFilter;
import fr.ropiteaux.rom.core.services.UserService;
import fr.ropiteaux.rom.core.services.impl.UserServiceImpl;
import fr.ropiteaux.rom.rest.UserResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;
import java.util.Properties;

import javax.servlet.DispatcherType;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;

public class RomApplication extends Application<RomConfiguration> {

	public static void main(String[] args) throws Exception {
		new RomApplication().run(args);
	}

	@Override
	public String getName() {
		return "recipe-o-matic";
	}

	@Override
	public void initialize(Bootstrap<RomConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/client/", "/", "index.html"));
	}

	@Override
	public void run(RomConfiguration config, Environment env) throws Exception {
		Injector injector = createInjector(config);

		// init persistence as managed object. If connect fail, app stop
		env.lifecycle().manage(injector.getInstance(PersistenceManager.class));

		// Init users as managed object
		env.lifecycle().manage(injector.getInstance(RomInitializer.class));

		// REST Provider is on /api/*
		env.jersey().setUrlPattern("/api/*");

		// AuthenticationResource
		// SecurityFilter on /api/* path
		env.servlets()
				.addFilter("SecurityFilter",
						injector.getInstance(RomSecurityFilter.class))
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
						true, "/api/*");

		// REST resources
		env.jersey().register(injector.getInstance(UserResource.class));
	}

	private Injector createInjector(final RomConfiguration conf) {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				// if someone would like to @Inject RomConfiguration
				bind(RomConfiguration.class).toInstance(conf);
				bind(UserService.class).to(UserServiceImpl.class);
				// bind logger listener
				bindListener(Matchers.any(), new Slf4jTypeListener());

			}
		}, createJpaPersistModule(conf.getDataSourceFactory()));
	}

	private JpaPersistModule createJpaPersistModule(DataSourceFactory conf) {
		Properties props = new Properties();
		props.put("javax.persistence.jdbc.url", conf.getUrl());
		props.put("javax.persistence.jdbc.user", conf.getUser());
		props.put("javax.persistence.jdbc.password", conf.getPassword());
		props.put("javax.persistence.jdbc.driver", conf.getDriverClass());
		JpaPersistModule jpaModule = new JpaPersistModule("Default");
		jpaModule.properties(props);
		return jpaModule;
	}

}
