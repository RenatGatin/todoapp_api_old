package ca.gatin.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.AuthorityPersistenceService;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.Authority;

/**
 * Service responsible for all interaction with Authorities
 *
 * @author RGatin
 * @since May 22, 2016
 */
@Component
public class AuthorityService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AuthorityPersistenceService authorityPersistenceService;
	
	/**
	 * Give authority(ies) by Authentication.
	 * Meaning if it is SUPERADMIN give all, if other give USER only,
	 * which defines with type of Account they can create.
	 * 
	 * @param authentication
	 * @return
	 */
	public ServiceResponse<?> getByAuthentication(boolean isSuperadmin) {
		ServiceResponse<Map<String, List<Authority>>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<Authority> authorities =  authorityPersistenceService.getAll();
		if (authorities == null || authorities.size() == 0) {
			serviceResponse.setStatus(ResponseStatus.AUTHORITIES_DB_FETCHING_FAILURE);
			
		} else {
			Authority userAuthority = null;
			/** 
			 * - hide all unnecessary information;
			 * - extract USER for non Superadmin
			 */
			for (Authority authority : authorities) {
				authority.setDateCreated(null);
				authority.setDateLastModified(null);
				
				if (authority.getName().equalsIgnoreCase(Authorities.ROLE_USER.name()))
					userAuthority = authority;
			}
			
			Map<String, List<Authority>> authorityList = new HashMap<>();
			if (isSuperadmin)
				authorityList.put("authorutyList", authorities);
			else
				authorityList.put("authorutyList", Arrays.asList(userAuthority));
			
			serviceResponse.setEntity(authorityList);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
		}
		return serviceResponse;
	}
	
}
