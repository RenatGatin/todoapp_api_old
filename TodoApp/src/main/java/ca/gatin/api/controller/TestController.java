package ca.gatin.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;

/**
 * Testing API Controller
 * 
 * @author RGatin
 * @since 17-Apr-2016
 */
@RestController
public class TestController extends BaseController {
	
	@RequestMapping(value= "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testManual() {
		logger.info("> Test");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity("successfull tested API");
		return serviceResponse;
	}
	
}
