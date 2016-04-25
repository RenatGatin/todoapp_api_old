package ca.gatin.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.Authorities;
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
	 * Removes unsave information as password, resetPasswordKey, etc.
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
