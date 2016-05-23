package ca.gatin.api.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.Authority;
import ca.gatin.model.security.User;

/**
 * Service responsible for all interaction with User
 * either it is user, admin, superadmin, etc..
 *
 * @author RGatin
 * @since Apr 24, 2016
 */
@Component
public class UserService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	/**
	 * Get list of given type of user
	 * 
	 * @param role
	 * @return
	 */
	public ServiceResponse<List<User>> getListOf(Authorities role) {
		ServiceResponse<List<User>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<User> adminList = userPersistenceService.getByRole(role);
		if (adminList == null || adminList.size() == 0){
			logger.debug("No users with role: " + role.name() + " found");
			serviceResponse.setStatus(ResponseStatus.ACCOUNT_NOT_FOUND);
			
		} else {
			logger.debug(adminList.size() + " users with role: " + role.name() + " found");
			serviceResponse.setEntity(secure(adminList));
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
		}
		return serviceResponse;
	}
	
	/**
	 * This method can be user either by SUPERADMIN or ADMIN
	 * 
	 * Allows to create any Account except of type SUPERADMIN
	 * 
	 * @param newUser
	 * @param authentication
	 * @return
	 */
	public ServiceResponse<?> createAnybody(User newUser) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		if (!hasAllRequiredFields(newUser, serviceResponse)) 
			return serviceResponse;
		
		if (hasRole(Authorities.ROLE_SUPERADMIN, newUser))
			serviceResponse = new ServiceResponse<>(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
		else
			serviceResponse = doCreate(newUser);
		
		return serviceResponse;
	}
	
	/**
	 * This method can be used by anybody even
	 * unauthenticated user
	 * 
	 * Before creating user checks:
	 * 1) if has all required fields;
	 * 2) has only 1 role: USER
	 * 
	 * @param newUser
	 * @return
	 */
	public ServiceResponse<?> createUser(User newUser) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		if (!hasAllRequiredFields(newUser, serviceResponse))
			return serviceResponse;
		
		if (hasRole(Authorities.ROLE_USER, newUser) && newUser.getAuthorities().size() == 1) {
			serviceResponse = doCreate(newUser);	
			
		} else {
			serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
		}
		return serviceResponse;
	}

	private ServiceResponse<?> doCreate(User newUser) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		if (userPersistenceService.existsByUsername(newUser.getUsername()) || userPersistenceService.existsByEmail(newUser.getEmail())) {
			serviceResponse.setStatus(ResponseStatus.ACCOUNT_UNIQUE_FIELD_DUPLICATION);
			
		} else {
			newUser.setDateCreated(new Date());
			
			if (userPersistenceService.save(newUser) != null) {
				serviceResponse.setStatus(ResponseStatus.SUCCESS);
			} else {
				serviceResponse.setStatus(ResponseStatus.ACCOUNT_DB_CREATION_FAILURE);
			}
		}
		return serviceResponse;
	}
	
	/**
	 * Checks if new User has all valid required fields
	 * 
	 * @param user
	 * @param serviceResponse
	 * @return
	 */
	private boolean hasAllRequiredFields(User user,	ServiceResponse<?> serviceResponse) {
		Set<Authority> newUserAuthorities = user.getAuthorities();
		boolean isValid = true;
		
		if (user.getUsername() == null || user.getPassword() == null || 
			user.getEmail() == null || newUserAuthorities == null ||
			newUserAuthorities.size() == 0) {

			serviceResponse.setStatus(ResponseStatus.MISSING_REQUIRED_FIELD);
			isValid = false;
			
		} else {
			for (Authority newAuthority : newUserAuthorities) {
				
				boolean foundMatchingValue = false;
				for (Authorities existingAuthority : Authorities.values()) {
					if (newAuthority.getName().equalsIgnoreCase(existingAuthority.name()))
						foundMatchingValue = true;
				}
				
				if (!foundMatchingValue) {
					serviceResponse.setStatus(ResponseStatus.INVALID_ROLE);
					return false;
				}
			}
		}
		return isValid;
	}
	
	/**
	 * Check if given User has role
	 * @param role 
	 * 
	 * @param newUser
	 * @param serviceResponse
	 * @return
	 */
	private boolean hasRole(Authorities role, User newUser) {

		for (Authority authority : newUser.getAuthorities()) {
			if (authority.getName().equalsIgnoreCase(role.name()))
				return true; 
		}
		return false;
	}

	/**
	 * Removes unsafe information as password, resetPasswordKey, etc.
	 * 
	 * @param adminList
	 * @return
	 */
	private List<User> secure(List<User> userList) {
		for(User user : userList) {
			user.setPassword(null);
			user.setActivationKey(null);
			user.setResetPasswordKey(null);
		}
		return userList;
	}
}
