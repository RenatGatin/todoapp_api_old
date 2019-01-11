package ca.gatin.api.exception;

import ca.gatin.api.response.ResponseStatus;

/**
 * Exception Abstract class
 * 
 * @author RGatin
 * @since 10-Jan-2019
 *
 */
public class AbstractApiServiceException extends RuntimeException {

	private static final long serialVersionUID = 2493221278720623601L;
	
	private ResponseStatus responseStatus;
	private boolean doPrintStackTrace;
	
	public AbstractApiServiceException(ResponseStatus responseStatus) {
		super();
		this.responseStatus = responseStatus;
		this.doPrintStackTrace = true;
	}
	
	public AbstractApiServiceException(ResponseStatus responseStatus, String message) {
		super(message);
		this.responseStatus = responseStatus;
		this.doPrintStackTrace = true;
	}
	
	public AbstractApiServiceException(ResponseStatus responseStatus, boolean doPrintStackTrace) {
		this.responseStatus = responseStatus;
		this.doPrintStackTrace = doPrintStackTrace;
	}
	
	public AbstractApiServiceException(ResponseStatus responseStatus, String message, boolean doPrintStackTrace) {
		super(message);
		this.responseStatus = responseStatus;
		this.doPrintStackTrace = doPrintStackTrace;
	}
	
	public ResponseStatus getResponseStatus() {
		return this.responseStatus;
	}
	
	public ServiceExceptionResponse getServiceResponse() {
		ServiceExceptionResponse serviceExceptionResponse = new ServiceExceptionResponse(this.responseStatus);
		return serviceExceptionResponse;
	}

	public boolean isdoPrintStackTrace() {
		return doPrintStackTrace;
	}

	public void setdoPrintStackTrace(boolean value) {
		this.doPrintStackTrace = value;
	}

}
