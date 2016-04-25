package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;

public interface UserPersistenceService {

	/**
	 * Gets all Users from DB
	 * 
	 * @return List<User>
	 */
	List<User> getAll();

	/**
	 * Gets User by id
	 * 
	 * @param id
	 * @return User
	 */
	User getById(Long id);
	
	/**
	 * Gets User by username (case insensitive)
	 * 
	 * @param username
	 * @return User
	 */
	User getByUsername(String username);
	
	/**
	 * Gets User by email (case insensitive)
	 * 
	 * @param email
	 * @return User
	 */
	User getByEmail(String email);

	/**
	 * Persists User
	 * 
	 * @param User
	 * @return saved User
	 */
	User save(User User);
	
	/**
	 * Checks if exists by id
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean existsById(Long id);
	
	/**
	 * Checks if exists by username
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean existsByUsername(String username);
	
	/**
	 * Checks if exists by email
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean existsByEmail(String email);

	/**
	 * Gets all activated Users
	 * 
	 * @return List<User>
	 */
	List<User> getActivated();

	/**
	 * Gets all not activated Users
	 * 
	 * @return List<User>
	 */
	List<User> getNotActivated();
	
	/**
	 * Gets all enabled Users
	 * 
	 * @return List<User>
	 */
	List<User> getEnabled();

	/**
	 * Gets all disabled Users
	 * 
	 * @return List<User>
	 */
	List<User> getDisabled();

	/**
	 * Deletes User 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean delete(Long id);
	
	/**
	 * Activates User 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean activate(Long id);
	
	/**
	 * Deactivates User 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deactivate(Long id);
	
	/**
	 * Enables User 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean enable(Long id);
	
	/**
	 * Disables User 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean disable(Long id);
	
	/**
	 * Get list of user by Role
	 * 
	 * @param role
	 * @return List<User>
	 */
	List<User> getByRole(Authorities role);
}
