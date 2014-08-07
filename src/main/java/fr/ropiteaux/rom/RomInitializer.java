package fr.ropiteaux.rom;


import fr.ropiteaux.rom.core.model.User;
import fr.ropiteaux.rom.core.services.UserService;
import io.dropwizard.lifecycle.Managed;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class RomInitializer implements Managed {

	@Inject
	private UserService userService;

	@Inject
	public RomInitializer() {
	}

	@Override
	public void stop() throws Exception {

	}

	@Transactional
	@Override
	public void start() {
		// Check if the admin user exists in database
		User adminUser = userService.findByEmail("admin@rom.app");
		if (adminUser == null) { // if no admin user exists
			// Create a default admin user with admin/admin as password
			User u = new User(null, "ADMIN", "Admin", "admin@rom.app",
					md5("admin"), null, new Date());
			u = userService.create(u);
		}
	}

	private String md5(String toHash) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(toHash.getBytes("UTF-8"));

			// converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}

			digest = sb.toString();

		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return digest;
	}
}