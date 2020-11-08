package ca.gatin.api.exception;

import ca.gatin.api.response.ResponseStatus;

/**
 * Exception to be thrown and caught in case when no user account found
 * 
 * @author RGatin
 * @since 10-Jan-2019
 *
 */
public class NoAccountFoundException extends AbstractApiServiceException {
	
	private static final long serialVersionUID = 6189951539053997000L;
	
	public NoAccountFoundException() {
		super(ResponseStatus.ACCOUNT_NOT_FOUND);
	}
	
	public NoAccountFoundException(String message) {
		super(ResponseStatus.ACCOUNT_NOT_FOUND, message);
	}
}
