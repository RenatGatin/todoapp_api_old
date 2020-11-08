package ca.gatin.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Response bean for rest API form beans validation error
 * 
 * @author RGatin
 * @since Sep 08, 2018
 * 
 */
public class ValidationErrorsResponse<T> extends ServiceResponse<T> {
	private List<FieldErrorBean> fieldErrors = new ArrayList<>(); 
	
	public ValidationErrorsResponse(ResponseStatus status) {
		super(status);
	}

	public List<FieldErrorBean> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorBean> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public void addFieldError(String path, String message) {
		FieldErrorBean error = new FieldErrorBean(path, message);
        fieldErrors.add(error);      
    }

}
