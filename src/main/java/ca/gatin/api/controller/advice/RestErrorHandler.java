package ca.gatin.api.controller.advice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.gatin.api.exception.AbstractApiServiceException;
import ca.gatin.api.exception.NoAccountFoundException;
import ca.gatin.api.exception.PermissionDeniedException;
import ca.gatin.api.exception.ServiceExceptionResponse;
import ca.gatin.api.exception.ValidationException;
import ca.gatin.api.response.FieldErrorBean;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ValidationErrorsResponse;
import ca.gatin.util.Check;

/**
 * Class to define Response beans for different exceptions 
 *
 * @author RGatin
 * @since Sep 08, 2018
 */
@ControllerAdvice
public class RestErrorHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * For @Valid request bean validation response
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ValidationErrorsResponse<?> processFormValidationError(HttpServletRequest request, MethodArgumentNotValidException ex) {
		logger.debug(ex.getMessage());
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();

		ValidationErrorsResponse<?> response = new ValidationErrorsResponse<>(ResponseStatus.INVALID_DATA);
		StringBuffer errorMessage = new StringBuffer();

		if (!Check.isEmpty(fieldErrors)) {
			for (FieldError fieldError : fieldErrors) {
				String fieldName = fieldError.getField();
				response.addFieldError(fieldName, fieldError.getDefaultMessage());
				if (!Check.isEmpty(errorMessage.toString())) {
					errorMessage.append(", ");
				}
				errorMessage.append("'" + fieldName + "' " + fieldError.getDefaultMessage());
			}
		} else {
			List<ObjectError> objectErrors = result.getAllErrors();
			for (ObjectError objError : objectErrors) {
				if (!Check.isEmpty(objError.getDefaultMessage())) {
					if (!Check.isEmpty(errorMessage.toString())) {
						errorMessage.append(", ");
					}
					errorMessage.append("'" + objError.getObjectName() + "' " + objError.getDefaultMessage());
				}
			}
		}
		response.setMessage(errorMessage.toString());

		return response;
	}
	
	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ValidationErrorsResponse<?> processNoAccountFoundException(HttpServletRequest request, ValidationException ex) {
		logger.debug(ex.getMessage());
		List<FieldErrorBean> fieldErrors = ex.getFieldErrors();
		
		ValidationErrorsResponse<?> response = new ValidationErrorsResponse<>(ResponseStatus.INVALID_DATA);
		StringBuffer errorMessage = new StringBuffer();

		if (!Check.isEmpty(fieldErrors)) {
			response.setFieldErrors(fieldErrors);
			for (FieldErrorBean fieldError : fieldErrors) {
				if (!Check.isEmpty(errorMessage.toString())) {
					errorMessage.append(", ");
				}
				errorMessage.append("'" + fieldError.getField() + "' " + fieldError.getMessage());
			}
		} 
		response.setMessage(errorMessage.toString());
		
		return response;
	}
	
	@ExceptionHandler({
						NoAccountFoundException.class,
						PermissionDeniedException.class
					 })
	@ResponseBody
	public ServiceExceptionResponse processValidationException(HttpServletRequest request, AbstractApiServiceException ex) {
		logger.debug(ex.getMessage());
		return ex.getServiceResponse();
	}
	
	/**
	 * Catch All Exceptions method
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ServiceExceptionResponse processGenericException(HttpServletRequest request, Exception ex) {
		logger.error("GenericException is: " + ex + ", message is: " + ex.getMessage());
		return new ServiceExceptionResponse(ResponseStatus.SYSTEM_INTERNAL_ERROR);
	}

}
