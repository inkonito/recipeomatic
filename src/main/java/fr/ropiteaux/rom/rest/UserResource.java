package fr.ropiteaux.rom.rest;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

import fr.ropiteaux.rom.core.model.User;
import fr.ropiteaux.rom.core.services.UserService;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	// TODO Restrict access

	@Inject
	private UserService userService;

	public UserResource() {
	}

	@GET
	@UnitOfWork
	public List<User> getAllUsers() {
		return userService.findAll();
	}

	@Path("/{userId}")
	@GET
	@UnitOfWork
	public User getPerson(@PathParam("userId") LongParam userId) {
		return findSafely(userId.get());
	}

	private User findSafely(long userId) {
		final User user = userService.findById(userId);
		if (user == null) {
			throw new NotFoundException("No such user.");
		}
		return user;
	}
}