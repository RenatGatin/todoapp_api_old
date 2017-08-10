package ca.gatin.api.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.controller.BaseController;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;

/**
 * Testing secured API controller
 *
 * @author RGatin
 * @since Apr 17, 2016
 *
 */
@RestController
@RequestMapping(value= "/api/secure")
public class TestSecureController extends BaseController {
	
	@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?>  testManual() {
		logger.info("> Secure Test");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity("successfull Secured API test");
		
		return serviceResponse;
	}
	
	@RequestMapping(value = "/testUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?>  testUser(HttpServletRequest httpServletRequest,
            			   HttpServletResponse httpServletResponse,
            			   Authentication authentication) {
		logger.info("> Secure Test User: " + authentication.getName());
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity("successfull Secured API testUser: " +  authentication.getName());
		
		return serviceResponse;
	}
	
}
