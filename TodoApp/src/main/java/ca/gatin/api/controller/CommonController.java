package ca.gatin.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.UserService;
import ca.gatin.model.request.ChangePasswordRequestBean;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;

/**
 * Common secured API Controller
 *
 * @author RGatin
 * @since May 24, 2016
 */
@RestController
@RequestMapping(value= "/api/common")
public class CommonController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/selfChangePassword", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> selfChangePassword(@RequestBody ChangePasswordRequestBean changePasswordRequestBean, Principal principal) {
		logger.info("> /api/common/selfChangePassword");
		User user = getCurrentUser(principal);
		
		return userService.selfChangePassword(changePasswordRequestBean, user);
	}
	
	@RequestMapping(value = "/selfDisable", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> selfDisable(Principal principal) {
		logger.info("> /api/common/selfDisable");
		User user = getCurrentUser(principal);
		
		return userService.selfDisable(user);
	}
	
	@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getSelfProfile(Principal principal) {
		logger.debug("> /api/common/profile");
		User user = getCurrentUser(principal);
		
		return userService.getSelfProfile(user);
	}
	
}