package ca.gatin.api.controller.test;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.controller.BaseController;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;

/**
 * Testing admin secured API Controller
 *
 * @author RGatin
 * @since Apr 17, 2016
 */
@RestController
@RequestMapping(value= "/api/admin")
public class TestAdminController extends BaseController {
	
	@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testManual(Authentication authentication) {
		logger.info("> Admin Test");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(authentication.getPrincipal());
		
		return serviceResponse;
	}
	
}
