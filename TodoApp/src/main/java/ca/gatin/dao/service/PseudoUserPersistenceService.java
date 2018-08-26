package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.security.PseudoUser;

public interface PseudoUserPersistenceService {

	/**
	 * Gets all PseudoUsers from DB
	 * 
	 * @return List<PseudoUser>
	 */
	List<PseudoUser> getAll();

	/**
	 * Gets PseudoUser by id
	 * 
	 * @param id
	 * @return PseudoUser
	 */
	PseudoUser getById(Long id);
	
	/**
	 * Gets PseudoUser by activationKey
	 * 
	 * @param activationKey
	 * @return PseudoUser
	 */
	PseudoUser getByActivationKey(String activationKey);
	
	/**
	 * Gets PseudoUser by username (case insensitive)
	 * 
	 * @param username
	 * @return PseudoUser
	 */
	PseudoUser getByUsername(String username);
	
	/**
	 * Gets PseudoUser by email (case insensitive)
	 * 
	 * @param email
	 * @return PseudoUser
	 */
	PseudoUser getByEmail(String email);

	/**
	 * Persists PseudoUser
	 * 
	 * @param PseudoUser
	 * @return saved User
	 */
	PseudoUser save(PseudoUser user);
	
	/**
	 * Checks if exists by username
	 * 
	 * @param username
	 * @return boolean
	 */
	boolean existsByUsername(String username);
	
	/**
	 * Checks if exists by activationKey
	 * 
	 * @param activationKey
	 * @return boolean
	 */
	boolean existsByActivationKey(String activationKey);
	
	/**
	 * Checks if exists by email
	 * 
	 * @param email
	 * @return boolean
	 */
	boolean existsByEmail(String email);

	/**
	 * Gets all activated PseudoUsers
	 * 
	 * @return List<PseudoUser>
	 */
	List<PseudoUser> getActivated();

	/**
	 * Gets all not activated PseudoUsers
	 * 
	 * @return List<PseudoUser>
	 */
	List<PseudoUser> getNotActivated();

	/**
	 * Deletes PseudoUser 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean delete(Long id);
	
	/**
	 * Marks Activated PseudoUser after real User is created 
	 * 
	 * @param id i existing PseudoUser id
	 * @return boolean
	 */
	boolean activate(Long id);
	
	/**
	 * Deactivates PseudoUser 
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deactivate(Long id);

}
