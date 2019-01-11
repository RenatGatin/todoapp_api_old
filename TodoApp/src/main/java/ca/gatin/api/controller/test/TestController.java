package ca.gatin.api.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.controller.BaseController;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.EmailService;
import ca.gatin.api.service.TodoService;

/**
 * Testing public API Controller
 * 
 * @author RGatin
 * @since 17-Apr-2016
 */
@RestController
@RequestMapping(value= "/api/test")
public class TestController extends BaseController {
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TodoService todoService;
	
	@RequestMapping(value= "/simple", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testManual() {
		logger.info("> Test");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity("successfull tested API");
		return serviceResponse;
	}
	
	//TODO: Delete after tests
	@RequestMapping(value= "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testMailSender() {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_INTERNAL_ERROR);
		
		try {
			emailService.test();
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} catch (Exception e) {
			logger.error("Error sending email", e);
		}
		
		return serviceResponse;
	}
	
	//TODO: Delete after tests
	@RequestMapping(value= "/todo/item/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testTodos() {
		return todoService.testTodoItemAll();
	}
	
	//TODO: Delete after tests
	@RequestMapping(value= "/todo/list/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testTodoList() {
		return todoService.testTodoListAll();
	}
	
}
