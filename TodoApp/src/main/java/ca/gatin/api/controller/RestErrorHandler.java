package ca.gatin.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();

		ValidationErrorsResponse<?> response = new ValidationErrorsResponse<>(ResponseStatus.INVALID_DATA);
		StringBuffer errorMessage = new StringBuffer();

		if (!Check.isEmpty(fieldErrors)) {
			for (FieldError fieldError : fieldErrors) {

				String fieldName = fieldError.getField();
				response.addFieldError(fieldName, fieldError.getDefaultMessage());

				if (!Check.isEmpty(errorMessage.toString())) {
					errorMessage.append("\n");
				}
				errorMessage.append(fieldError.getDefaultMessage());
			}
		} else {
			List<ObjectError> objectErrors = result.getAllErrors();
			for (ObjectError objError : objectErrors) {
				
				if (!Check.isEmpty(objError.getDefaultMessage())) {
					if (!Check.isEmpty(errorMessage.toString())) {
						errorMessage.append("\n");
					}
					errorMessage.append(objError.getDefaultMessage());
				}
			}
		}

		return response;
	}

}
