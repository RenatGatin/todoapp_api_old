package ca.gatin.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.AuthorityService;
import ca.gatin.api.service.UserService;
import ca.gatin.model.security.User;

/**
 * Open API Controller
 *
 * @author RGatin
 * @since May 22, 2016
 */
@RestController
@RequestMapping(value= "/open")
public class OpenController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		logger.debug("> /open/ping");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
	
		return serviceResponse;
	}
	
	@RequestMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> createUser(@RequestBody User newUser) {
		logger.info("> /open/createUser: " + newUser.toString());
		
		return userService.create(newUser, false);
	}
	
	@RequestMapping(value = "/getAuthorities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getAuthorities(Authentication authentication) {
		logger.debug("> /open/getAuthorities by: " + authentication);
	
		return authorityService.getByAuthentication(false);
	}
	
}