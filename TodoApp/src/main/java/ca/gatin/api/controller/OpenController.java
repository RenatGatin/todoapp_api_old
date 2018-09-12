package ca.gatin.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.AuthorityService;
import ca.gatin.api.service.UserService;
import ca.gatin.model.signup.PreSignupUser;

/**
 * Open API Controller
 *
 * @author RGatin
 * @since May 22, 2016
 */
@RestController
@RequestMapping(value= "/api/open")
public class OpenController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		logger.debug("> /api/open/ping");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
	
		return serviceResponse;
	}
	
	@RequestMapping(value = "/preSignupUser", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> createUser(@Valid @RequestBody PreSignupUser preSignupUser) {
		logger.info("> /api/open/preSignupUser: " + preSignupUser.toString());
		
		return userService.create(preSignupUser);
	}
	
	@RequestMapping(value = "/isPreSignupUserActivated/{username}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ServiceResponse<?> isPreSignupUserActivated(@PathVariable String username) {
		logger.info("> /api/open/isPreSignupUserActivated: " + username);
		
		return userService.isPreSignupUserActivated(username);
	}
	
	@RequestMapping(value = "/activatePreSignupUser/{username}/{key}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> activatePreSignupUser(@PathVariable String username, @PathVariable String key) {
		logger.info("> /api/open/activatePreSignupUser: username: " + username + ", key: " + key);
		
		return userService.activatePreSignupUser(username, key);
	}
	
	@RequestMapping(value = "/passwordReset/{email}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> passwordReset(@PathVariable String email) {
		logger.info("> /api/open/passwordReset: email: " + email);
		
		return userService.passwordReset(email);
	}
	
	@RequestMapping(value = "/getAuthorities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getAuthorities(Authentication authentication) {
		logger.debug("> /api/open/getAuthorities by: " + authentication);
	
		return authorityService.getByAuthentication(false);
	}
	
}