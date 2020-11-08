package ca.gatin.api.exception;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.SimpleServiceResponse;

/**
 * Exception Service response
 * 
 * @author RGatin
 * @since 10-Jan-2019
 *
 */
public class ServiceExceptionResponse extends SimpleServiceResponse {

	public ServiceExceptionResponse() {
		super();
	}

	public ServiceExceptionResponse(ResponseStatus status) {
		super(status);
	}

}
