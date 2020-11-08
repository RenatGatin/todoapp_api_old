package ca.gatin.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
 * Admin secured API Controller
 *
 * @author RGatin
 * @since Apr 23, 2016
 */
@RestController
@RequestMapping(value= "/api/admin")
public class AdminController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		return serviceResponse;
	}
	
	@RequestMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> createUser(@RequestBody User newUser) {
		return userService.create(newUser, false);
	}
	
	@RequestMapping(value = "/getUserProfileList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getUserProfileList() {
		return userService.getListOf(Authorities.ROLE_USER, false);
	}
	
	@RequestMapping(value = "/deleteByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> deleteByUsername(@PathVariable String username) {
		return userService.deleteByUsername(username, false);
	}
	
	@RequestMapping(value = "/disableByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> disableByUsername(@PathVariable String username) {
		return userService.enableOrDisableByUsername(false, username, false);
	}
	
	@RequestMapping(value = "/enableByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> enableByUsername(@PathVariable String username) {
		return userService.enableOrDisableByUsername(true, username, false);
	}
	
	@RequestMapping(value = "/changeUserPassword/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> selfChangePassword(@RequestBody ChangePasswordRequestBean changePasswordRequestBean, @PathVariable String username) {
		return userService.changePassword(changePasswordRequestBean, username, false);
	}
	
	@RequestMapping(value = "/selfDelete", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> selfDelete(Principal principal) {
		User user = getCurrentUser(principal);
		return userService.selfDelete(user);
	}
	
	@RequestMapping(value = "/getUserProfileByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getUserProfileByUsername(@PathVariable String username) {
		return userService.getAdminOrUserProfileByUsername(username, false);
	}
	
}