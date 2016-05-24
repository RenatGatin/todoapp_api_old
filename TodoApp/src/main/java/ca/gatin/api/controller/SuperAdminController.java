package ca.gatin.api.controller;

import java.security.Principal;

import javax.websocket.server.PathParam;

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
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;

/**
 * SuperAdmin secured API Controller
 *
 * @author RGatin
 * @since Apr 23, 2016
 */
@RestController
@RequestMapping(value= "/superadmin")
public class SuperAdminController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		logger.info("> /superadmin/ping");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
	
		return serviceResponse;
	}
	
	@RequestMapping(value = "/getAdminList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getAdminList() {
		logger.info("> /superadmin/getAdminList");
		
		return userService.getListOf(Authorities.ROLE_ADMIN);
	}
	
	@RequestMapping(value = "/getAuthorities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getAuthorities(Authentication authentication) {
		logger.debug("> /superadmin/getAuthorities by: " + authentication);
	
		return authorityService.getByAuthentication(true);
	}
	
	@RequestMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> create(@RequestBody User newUser) {
		logger.info("> /superadmin/create: " + newUser.toString());
		
		return userService.createAnybody(newUser);
	}
	
	@RequestMapping(value = "/deleteByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> deleteByUsername(@PathVariable String username) {
		logger.info("> /superadmin/deleteByUsername");
		
		return userService.deleteByUsername(username, true);
	}
	
}