package fr.ropiteaux.rom.core.manager;

import io.dropwizard.lifecycle.Managed;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

public class PersistenceManager implements Managed {
	private PersistService persist;

	@Inject
	public PersistenceManager(PersistService persist) {
		this.persist = persist;
	}

	@Override
	public void start() throws Exception {
		this.persist.start();
	}

	@Override
	public void stop() throws Exception {
		this.persist.stop();
	}
}