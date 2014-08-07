package fr.ropiteaux.rom.core.services;

import fr.ropiteaux.rom.core.model.User;

public interface UserService extends CRUDService<User> {

	User findByEmail(String email);

	User findByRequestToken(String userid, String authToken);
}