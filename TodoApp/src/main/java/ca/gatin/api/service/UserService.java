package ca.gatin.api.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.PseudoUserPersistenceService;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.request.ChangePasswordRequestBean;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.Authority;
import ca.gatin.model.security.PseudoUser;
import ca.gatin.model.security.User;
import ca.gatin.model.signup.PreSignupUser;

/**
 * Service responsible for all interaction with User
 * either it is user, admin, superadmin, etc..
 *
 * @author RGatin
 * @since Apr 24, 2016
 */
@Component
public class UserService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	@Autowired
	private PseudoUserPersistenceService pseudoUserPersistenceService;
	
	@Autowired
	private EmailService emailService;
	
	/**
	 * Get list of given type of user
	 * 
	 * @param role
	 * @return
	 */
	public ServiceResponse<?> getListOf(Authorities role, boolean isRequestFromSuperadmin) {
		ServiceResponse<List<User>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		if (!role.equals(Authorities.ROLE_SUPERADMIN)) {
			
			if (role.equals(Authorities.ROLE_ADMIN) && !isRequestFromSuperadmin) {
				serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
				
			} else {
				List<User> adminList = userPersistenceService.getByRole(role);
				if (adminList == null || adminList.size() == 0){
					logger.debug("No users with role: " + role.name() + " found");
					serviceResponse.setStatus(ResponseStatus.ACCOUNT_NOT_FOUND);
					
				} else {
					logger.debug(adminList.size() + " users with role: " + role.name() + " found");
					serviceResponse.setEntity(secure(adminList));
					serviceResponse.setStatus(ResponseStatus.SUCCESS);
				}
			}
		} else {
			serviceResponse.setStatus(ResponseStatus.ACTION_NOT_PERMITTED);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> create(PreSignupUser preSignupUser) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		if (!hasAllRequiredFields(preSignupUser, serviceResponse)) {
			return serviceResponse;
		}
		
		if (pseudoUserPersistenceService.existsByEmail(preSignupUser.getEmail())) {
			serviceResponse.setStatus(ResponseStatus.ACCOUNT_UNIQUE_FIELD_DUPLICATION);
			return serviceResponse;
		}
		
		String activationKey = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replaceAll("-", "");
		if (pseudoUserPersistenceService.existsByActivationKey(activationKey)) {
			activationKey = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replaceAll("-", "");
		}
		
		PseudoUser pseudoUser = new PseudoUser();
		pseudoUser.setUsername(preSignupUser.getEmail());
		pseudoUser.setEmail(preSignupUser.getEmail());
		pseudoUser.setFirstname(preSignupUser.getFirstName());
		pseudoUser.setLastname(preSignupUser.getLastName());
		pseudoUser.setPassword(passwordEncoder.encode(preSignupUser.getPassword()));
		pseudoUser.setActivationKey(activationKey);
		pseudoUser.setDateCreated(new Date());
		
		PseudoUser savedPseudoUser = null;
		try {
			savedPseudoUser = pseudoUserPersistenceService.save(pseudoUser);
			
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.ERROR_SAVING_USER_IN_DATABASE);
			e.printStackTrace();
			return serviceResponse;
		}
		
		try {
			emailService.sendActivationKey(savedPseudoUser);
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.EMAIL_TRANSMISSION_ERROR);
			e.printStackTrace();
			pseudoUserPersistenceService.delete(pseudoUser.getId());
			return serviceResponse;
		}
		
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		return serviceResponse;
	}
	
	/**
	 * This method can be used by anybody even
	 * unauthenticated user if trying to create USER
	 * 
	 * But ADMIN account can created only by SUPERADMIN
	 * 
	 * Before creating user checks:
	 * 1) if has all required fields;
	 * 2) has only 1 role: USER or ADMIN
	 * 
	 * @param newUser
	 * @param isAdminCreation 
	 * @return
	 */
	public ServiceResponse<?> create(User newUser, boolean isAdminCreation) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		try {
			if (!hasAllRequiredFields(newUser, serviceResponse))
				return serviceResponse;
			
			if (newUser.getAuthorities().size() > 1 || 
				(isAdminCreation && !hasRole(Authorities.ROLE_ADMIN, newUser)) ||
				(!isAdminCreation && !hasRole(Authorities.ROLE_USER, newUser))) {
				serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
				
			} else {
				String encodedPassword = passwordEncoder.encode(newUser.getPassword());
				newUser.setPassword(encodedPassword);
				serviceResponse = doCreate(newUser);	
			}
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	
	/**
	 * Deletes currently logged in user.
	 * Only allowed to User and Admin delete itself.
	 * 
	 * @param principal 
	 * @return
	 */
	public ServiceResponse<?> selfDelete(Principal principal) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String username = principal.getName();
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				
				if (!hasRole(Authorities.ROLE_SUPERADMIN, user)) {
					boolean deleted = userPersistenceService.delete(user.getId());
					ResponseStatus response = (deleted) ? ResponseStatus.SUCCESS : ResponseStatus.ACCOUNT_DB_DELETION_FAILURE;
					serviceResponse.setStatus(response);
					
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
	
	/**
	 * Method suppose to be used only by ADMIN
	 * or SUPERADMIN.
	 * 
	 * Can either enable or disable other account
	 * 
	 * @param true - enable, false - disable 
	 * @param username
	 * @param isRequestFromSuperadmin
	 * @return
	 */
	public ServiceResponse<?> enableOrDisableByUsername(boolean enable, String username, boolean isRequestFromSuperadmin) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				
				// prohibited to disable SUPERADMIN
				if (!hasRole(Authorities.ROLE_SUPERADMIN, user)) {
				
					// ADMIN can be disable only by SUPERADMIN
					if (hasRole(Authorities.ROLE_ADMIN, user) && !isRequestFromSuperadmin) {
						serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
						
					} else {
						Long id = user.getId();
						boolean operationSucceeded = (enable) ? userPersistenceService.enable(id) : userPersistenceService.disable(id);
						ResponseStatus response = (operationSucceeded) ? ResponseStatus.SUCCESS : ResponseStatus.ACCOUNT_DB_UPDATION_FAILURE;
						serviceResponse.setStatus(response);
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
	
	/**
	 * Anybody can disable themselves 
	 * except SUPERADMIN
	 * 
	 * @param principal
	 * @return
	 */
	public ServiceResponse<?> selfDisable(Principal principal) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String username = principal.getName();
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				
				// prohibited to disable SUPERADMIN
				if (!hasRole(Authorities.ROLE_SUPERADMIN, user)) {
					boolean disabled = userPersistenceService.disable(user.getId());
					ResponseStatus response = (disabled) ? ResponseStatus.SUCCESS : ResponseStatus.ACCOUNT_DB_UPDATION_FAILURE;
					serviceResponse.setStatus(response);
					
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
	
	/**
	 * Self change password
	 * 
	 * @param changePasswordRequestBean
	 * @param principal
	 * @return
	 */
	public ServiceResponse<?> selfChangePassword(ChangePasswordRequestBean changePasswordRequestBean, Principal principal) {
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
					if (!passwordEncoder.matches(currentPassword, encodedPassword))
						serviceResponse.setStatus(ResponseStatus.OLD_PASSWORD_DOES_NOT_MATCH_CURRENT_VALUE);
					else
						serviceResponse = doChangePassword(newPassword1, user);
					
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
	
	/**
	 * Method allows to change password for other account
	 * SUPERADMIN can change ADMIN and USER;
	 * ADMIN can change USER only;
	 * 
	 * @param changePasswordRequestBean
	 * @param username
	 * @param isSuperAdminLogin
	 * @return
	 */
	public ServiceResponse<?> changePassword(ChangePasswordRequestBean changePasswordRequestBean, String username, boolean isSuperAdminLogin) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String newPassword1 = changePasswordRequestBean.getNewPassword1();
			String newPassword2 = changePasswordRequestBean.getNewPassword2();
			
			if (newPassword1 == null || newPassword2 == null) {
				serviceResponse.setStatus(ResponseStatus.MISSING_REQUIRED_FIELD);
				
			} else if (!newPassword1.equals(newPassword2)) {
				serviceResponse.setStatus(ResponseStatus.NEW_PASSWORD_FIELDS_DOES_NOT_MATCH);
				
			} else {
				User user = userPersistenceService.getByUsername(username);
				if (user != null) {
					
					if (hasRole(Authorities.ROLE_SUPERADMIN, user) ||
						(hasRole(Authorities.ROLE_ADMIN, user) && !isSuperAdminLogin)) {
						serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
						
					} else {
						String currentEncodedPassword = user.getPassword();
						if (passwordEncoder.matches(newPassword1, currentEncodedPassword))
							serviceResponse.setStatus(ResponseStatus.NEW_PASSWORD_HAS_TO_BE_DIFFERENT);
						else
							serviceResponse = doChangePassword(newPassword1, user);
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

	/**
	 * Get Profile of itself
	 * 
	 * @param principal
	 * @return
	 */
	public ServiceResponse<?> getSelfProfile(Principal principal) {
		ServiceResponse<User> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			String username = principal.getName();
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				cleanUser(user);
				serviceResponse.setEntity(user);
				serviceResponse.setStatus(ResponseStatus.SUCCESS);
				
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
	 * Get Admin of User Profile
	 * 
	 * @param username
	 * @param isRequestFromSuperadmin
	 * @return
	 */
	public ServiceResponse<?> getAdminOrUserProfileByUsername(String username, boolean isRequestFromSuperadmin) {
		ServiceResponse<User> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			User user = userPersistenceService.getByUsername(username);
			
			if (user != null) {
				
				// prohibited to provide SUPERADMIN profile
				if (!hasRole(Authorities.ROLE_SUPERADMIN, user)) {
				
					// ADMIN can be retrieved only by SUPERADMIN
					if (hasRole(Authorities.ROLE_ADMIN, user) && !isRequestFromSuperadmin) {
						serviceResponse.setStatus(ResponseStatus.NOT_ENOUGH_PRIVILEGIES);
						
					} else {
						cleanUser(user);
						serviceResponse.setEntity(user);
						serviceResponse.setStatus(ResponseStatus.SUCCESS);
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
	
	private ServiceResponse<?> doChangePassword(String newPassword, User user) {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		String newPasswordEncoded = passwordEncoder.encode(newPassword);
		boolean changed = userPersistenceService.changePassword(user.getId(), newPasswordEncoded);
		
		ResponseStatus response = (changed) ? ResponseStatus.SUCCESS : ResponseStatus.ACCOUNT_DB_UPDATION_FAILURE;
		serviceResponse.setStatus(response);
		
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
	 * Checks if new PreSignupUser has all valid required fields
	 * 
	 * @param preSignupUser
	 * @param serviceResponse
	 * @return
	 */
	private boolean hasAllRequiredFields(PreSignupUser preSignupUser,	ServiceResponse<?> serviceResponse) {
		boolean isValid = true;
		
		if (StringUtils.isEmpty(preSignupUser.getEmail()) || StringUtils.isEmpty(preSignupUser.getPassword()) || 
			StringUtils.isEmpty(preSignupUser.getFirstName()) || StringUtils.isEmpty(preSignupUser.getLastName())) {
			
			serviceResponse.setStatus(ResponseStatus.MISSING_REQUIRED_FIELD);
			isValid = false;
		} 
		return isValid;
	}
	
	/**
	 * Check if given User has role
	 * @param role 
	 * 
	 * @param user
	 * @param serviceResponse
	 * @return
	 */
	private boolean hasRole(Authorities role, User user) {

		for (Authority authority : user.getAuthorities()) {
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
		
		for(User user : userList)
			cleanUser(user);
		
		return userList;
	}

	private void cleanUser(User user) {
		user.setPassword(null);
		user.setActivationKey(null);
		user.setResetPasswordKey(null);
	}

}
