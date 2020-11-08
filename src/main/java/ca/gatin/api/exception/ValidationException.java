package ca.gatin.api.exception;

import java.util.ArrayList;
import java.util.List;

import ca.gatin.api.response.FieldErrorBean;
import ca.gatin.api.response.ResponseStatus;

/**
 * Exception to be thrown and caught in case of invalid data
 * 
 * @author RGatin
 * @since 21-Jan-2019
 *
 */
public class ValidationException extends AbstractApiServiceException {
	
	private static final long serialVersionUID = 6189951539053997000L;
	private List<FieldErrorBean> fieldErrors = new ArrayList<>();
	
	public ValidationException(List<FieldErrorBean> fieldErrors) {
		super(ResponseStatus.INVALID_DATA);
		this.setFieldErrors(fieldErrors);
	}
	
	public ValidationException(FieldErrorBean fieldError) {
		super(ResponseStatus.INVALID_DATA);
		this.getFieldErrors().add(fieldError);
	}
	
	public List<FieldErrorBean> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorBean> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
