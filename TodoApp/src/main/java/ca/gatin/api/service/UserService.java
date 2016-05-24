package ca.gatin.api.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.request.ChangePasswordRequestBean;
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
		try {
			if (!hasAllRequiredFields(newUser, serviceResponse))
				return serviceResponse;
			
			if (hasRole(Authorities.ROLE_USER, newUser) && newUser.getAuthorities().size() == 1) {
				serviceResponse = doCreate(newUser);	
				
			} else {
				serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
			}
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	
	/**
	 * Deletes currently logged in user.
	 * Only allowed to User delete itself.
	 * 
	 * @param principal
	 * @param principal 
	 * @param roleUser 
	 * @return
	 */
	public ServiceResponse<?> selfDelete(Authentication authentication, Principal principal) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String username = principal.getName();
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				boolean deleted = userPersistenceService.delete(user.getId());
				if (deleted)
					serviceResponse.setStatus(ResponseStatus.SUCCESS);
					//TODO: trigger logout after this
				else
					serviceResponse.setStatus(ResponseStatus.ACCOUNT_DB_DELETION_FAILURE);
				
			} else {
				serviceResponse.setStatus(ResponseStatus.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	
	/**
	 * Method suppose to be used only by ADMIN
	 * or SUPERADMIN.
	 * Rule:
	 * - does not allow to delete SUPERADMIN account;
	 * - ADMIN can't delete another ADMIN;
	 */
	public ServiceResponse<?> deleteByUsername(String username, boolean isRequestFromSuperadmin) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				
				// prohibited to delete SUPERADMIN
				if (!hasRole(Authorities.ROLE_SUPERADMIN, user)) {
				
					// ADMIN can be deleted only by SUPERADMIN
					if (hasRole(Authorities.ROLE_ADMIN, user) && !isRequestFromSuperadmin) {
						serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
						
					} else {
						boolean deleted = userPersistenceService.delete(user.getId());
						if (deleted)
							serviceResponse.setStatus(ResponseStatus.SUCCESS);
							//TODO: trigger logout after this
						else
							serviceResponse.setStatus(ResponseStatus.ACCOUNT_DB_DELETION_FAILURE);
					}
				} else {
					serviceResponse.setStatus(ResponseStatus.ACTION_NOT_PERMITTED);
				}
			} else {
				serviceResponse.setStatus(ResponseStatus.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> changePassword(ChangePasswordRequestBean changePasswordRequestBean, Principal principal) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String currentPassword = changePasswordRequestBean.getCurrentPassword();
			String newPassword1 = changePasswordRequestBean.getNewPassword1();
			String newPassword2 = changePasswordRequestBean.getNewPassword2();
			
			if (currentPassword == null || newPassword1 == null || newPassword2 == null) {
				serviceResponse.setStatus(ResponseStatus.MISSING_REQUIRED_FIELD);
				
			} else if (!newPassword1.equals(newPassword2)) {
				serviceResponse.setStatus(ResponseStatus.NEW_PASSWORD_FIELDS_DOES_NOT_MATCH);
				
			} else if (newPassword1.equals(currentPassword)) {
				serviceResponse.setStatus(ResponseStatus.NEW_PASSWORD_HAS_TO_BE_DIFFERENT);
				
			} else {
				String username = principal.getName();
				User user = userPersistenceService.getByUsername(username);
				
				if (user != null) {
					
					String encodedPassword = user.getPassword();
					PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
					
					if (!passwordEncoder.matches(currentPassword, encodedPassword)) {
						serviceResponse.setStatus(ResponseStatus.OLD_PASSWORD_DOES_NOT_MATCH_CURRENT_VALUE);
						
					} else {
						String newPasswordEncoded = passwordEncoder.encode(newPassword1);
						boolean changed = userPersistenceService.changePassword(user.getId(), newPasswordEncoded);
						if (changed)
							serviceResponse.setStatus(ResponseStatus.SUCCESS);
						else
							serviceResponse.setStatus(ResponseStatus.ACCOUNT_DB_UPDATION_FAILURE);
					}
					
				} else {
					serviceResponse.setStatus(ResponseStatus.ACCOUNT_NOT_FOUND);
				}
			}
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}

	private ServiceResponse<?> doCreate(User newUser) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
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
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
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
