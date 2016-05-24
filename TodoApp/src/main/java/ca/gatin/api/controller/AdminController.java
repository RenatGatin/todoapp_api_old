package ca.gatin.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.UserService;
import ca.gatin.model.security.Authorities;

/**
 * Admin secured API Controller
 *
 * @author RGatin
 * @since Apr 23, 2016
 */
@RestController
@RequestMapping(value= "/admin")
public class AdminController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		logger.debug("> /admin/ping");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
	
		return serviceResponse;
	}
	
	@RequestMapping(value = "/getUserList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getAdminList() {
		logger.debug("> /admin/getUserList");
		
		return userService.getListOf(Authorities.ROLE_USER);
	}
	
	@RequestMapping(value = "/deleteByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> deleteByUsername(@PathVariable String username) {
		logger.info("> /admin/deleteByUsername");
		
		return userService.deleteByUsername(username, false);
	}
	
}