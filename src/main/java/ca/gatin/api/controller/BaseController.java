package ca.gatin.api.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.gatin.api.exception.NoAccountFoundException;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.User;

/**
 * Base Controller for all API controllers
 * 
 * @author RGatin
 * @since 02-Apr-2016
 */
public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	public User getCurrentUser(Principal principal) {
		String username = principal.getName();
		if (username != null) {
			User user = userPersistenceService.getByUsername(username);
			if (user != null) {
				return user;
			}
		}
		throw new NoAccountFoundException();
	}
	
}