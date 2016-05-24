package ca.gatin.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.UserService;
import ca.gatin.model.request.ChangePasswordRequestBean;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;

/**
 * User secured API Controller
 *
 * @author RGatin
 * @since Apr 23, 2016
 */
@RestController
@RequestMapping(value= "/user")
public class UserController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		logger.debug("> Admin ping");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
	
		return serviceResponse;
	}
	
	@RequestMapping(value = "/selfDelete", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> selfDelete(Principal principal) {
		logger.info("> /user/delete");
		
		return userService.selfDelete(principal);
	}
	
}