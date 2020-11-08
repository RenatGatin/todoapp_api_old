package ca.gatin.api.exception;

import ca.gatin.api.response.ResponseStatus;

/**
 * Exception to be thrown and caught in case of lack of permission
 * 
 * @author RGatin
 * @since 21-Jan-2019
 *
 */
public class PermissionDeniedException extends AbstractApiServiceException {
	
	private static final long serialVersionUID = 6189951539053997000L;
	
	public PermissionDeniedException() {
		super(ResponseStatus.ACTION_NOT_PERMITTED);
	}
	
	public PermissionDeniedException(String message) {
		super(ResponseStatus.ACTION_NOT_PERMITTED, message);
	}
}
