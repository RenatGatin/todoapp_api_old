package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.Authority;
import ca.gatin.model.security.User;

public interface AuthorityPersistenceService {

	/**
	 * Gets all Authorites from DB
	 * 
	 * @return List<Authority>
	 */
	List<Authority> getAll();
}
